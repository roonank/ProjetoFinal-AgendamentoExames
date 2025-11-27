# 🧪 CheckInLab – Sistema de Agendamento de Exames Laboratoriais

O **CheckInLab** é um sistema completo para **gestão de agendamentos de exames laboratoriais**, permitindo que usuários (clientes) realizem agendamentos em unidades de laboratório, com seleção de exames, horários, unidade de atendimento e observações.  
Administradores podem gerenciar **exames**, **unidades de laboratório**.

---

## 🚀 Tecnologias Utilizadas

### Backend (API - Spring Boot)
- Java 17
- Spring Boot 3 (Security, JWT, JPA, Validation)
- PostgreSQL
- RabbitMQ

### Frontend (Angular)
- Angular 17
- TailwindCSS
- Nginx (para deploy estático)
- Docker

### Infraestrutura
- Docker & Docker Compose
- Adminer (gerenciamento do banco)
- RabbitMQ Management Console

---

## 📁 Estrutura do Projeto

- /ProjetoFinal-AgendamentoExames
- │
- ├── api/ # Aplicação Spring Boot (API)
- ├── frontend/ # Aplicação Angular
- └── infra/
-     └── compose/
-         └── docker-compose.yml


---

## 🐳 Como Rodar com Docker

### 📝 Pré-requisitos

- Docker instalado 👉 https://www.docker.com/get-started  
- Docker Compose instalado (já vem junto com Docker Desktop)

---

### ▶️ Subir todos os serviços

No terminal, acesse a pasta onde está o `docker-compose.yml`: cd infra/compose

Execute: docker-compose up --build


---

### 🌐 Acessos

| Serviço           | URL                                                                                             |
|-------------------|-------------------------------------------------------------------------------------------------|
| Frontend Angular  | http://localhost:4200                                                                           |
| API Spring Boot   | http://localhost:8081/api                                                                       |
| Adminer (banco)   | http://localhost:8080 (db:agendamento_db / user: agendamento_user / pass: agendamento_password) |
| RabbitMQ Console  | http://localhost:15672 (user: admin / pass: admin)                                              |

---

### ⛔ Parar e remover containers

```bash
docker-compose down
