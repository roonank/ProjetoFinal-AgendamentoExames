package com.example.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendAppointmentCreatedEmail(String to,
                                            String userName,
                                            String labName,
                                            String labAddress,
                                            String dateTime,
                                            String examsSummary) {

        String subject = "Confirmação de agendamento de exames";

        String body = """
                Olá, %s!

                Seu agendamento foi realizado com sucesso.

                Unidade: %s
                Endereço: %s
                Data/Hora: %s

                Exames:
                %s

                Caso precise remarcar ou cancelar, entre em contato com a unidade.

                Atenciosamente,
                Sistema de Agendamento de Exames
                """.formatted(userName, labName, labAddress, dateTime, examsSummary);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@agendamento.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Bem-vindo ao sistema de agendamento de exames";

        String body = """
                Olá, %s!

                Seu cadastro foi realizado com sucesso no sistema de agendamento de exames.

                A partir de agora você pode:
                - Agendar exames em nossas unidades credenciadas;
                - Consultar o status dos agendamentos;
                - Acompanhar os prazos de entrega dos resultados.

                Qualquer dúvida, basta responder este e-mail.

                Atenciosamente,
                Equipe de Agendamento de Exames
                """.formatted(userName);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@agendamento.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public String joinExamNames(java.util.List<String> examNames) {
        StringJoiner sj = new StringJoiner("\n - ", " - ", "");
        examNames.forEach(sj::add);
        return sj.toString();
    }

    public String formatDateTime(java.time.LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}

