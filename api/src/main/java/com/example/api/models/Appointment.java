package com.example.api.models;

import com.example.api.models.enums.AppoitmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Appointments", indexes = {
        @Index(columnList = "user_id", name = "idx_appointment_user"),
        @Index(columnList = "scheduled_at", name = "idx_appointment_scheduled")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "labUnit_id", nullable = false)
    private LabUnit labUnit;

    @NotNull
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduletAt;

    private Integer totalDurationDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppoitmentStatus status = AppoitmentStatus.SCHEDULED;

    private LocalDateTime createdAt = LocalDateTime.now();
    private String cancelReason;

    @Column(columnDefinition = "text")
    private String notes;

    @OneToMany(
            mappedBy = "appointment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<AppointmentExam> exams = new ArrayList<>();

    public void addExam(Exam exam, Integer durationDays, String note){
        AppointmentExam item = AppointmentExam.builder()
                .appointment(this)
                .exam(exam)
                .durationDays(durationDays)
                .note(note)
                .build();
        this.exams.add(item);
    }

    public void removeExam(AppointmentExam item){
        this.exams.remove(item);
        item.setAppointment(null);
    }


}
