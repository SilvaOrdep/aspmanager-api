# Back-End ASP Manager

![Java](https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.12-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql&logoColor=white)

## Sobre

API REST para gerenciamento de espaços acadêmicos de universidades. Possui autenticação JWT e organização em camadas, com módulos por domínio.

## Módulos

- **auth**: autenticação e emissão de token JWT (`/api/auth/login`).
- **usuario**: CRUD de usuários, ativação/inativação, alteração de senha e gestão de professores.
- **instituicao**: CRUD de instituições de ensino.
- **escola**: CRUD de escolas e disciplinas.
- **espaco**: CRUD de espaços, solicitações de reserva e consulta de disponibilidade.
- **software**: CRUD de softwares e solicitações de cadastro/ativação.
- **shared**: base de CRUD, segurança/JWT e tratamento global de erros.

## Stack

- Java 21
- Spring Boot 3.5.12
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security + JWT (java-jwt 4.4.0)
- Validation
- MapStruct 1.6.3
- Lombok
- Springdoc OpenAPI 2.8.15
- PostgreSQL 15
- H2 (testes)
- Maven 3.9

## Padrões e princípios

- Arquitetura em camadas: Controller, Service, Repository
- DTOs para entrada e saída
- Mappers com MapStruct
- Repository Pattern
- Validação e tratamento global de erros
- Autenticação JWT

## Como rodar

### Execução local

Pré-requisitos:

- Java 21
- Maven (ou Maven Wrapper)
- PostgreSQL

1. Configure as variáveis de ambiente (veja seção abaixo).
2. Execute:

```bash
./mvnw spring-boot:run
```

Servidor: `http://localhost:8080`.

### Execução com Docker Compose

1. Configure as variáveis do `.env` (use `.env.example` como base).
2. Suba a stack:

```bash
docker compose up --build
```

Portas expostas:

- Backend: `http://localhost:9090` (`9090:8080`)
- PostgreSQL: `localhost:5000` (`5000:5432`)

### Testes

```bash
./mvnw test
```

## Docker

O projeto possui `Dockerfile` multi-stage e `docker-compose.yml` para subir a API junto com o PostgreSQL.

## Variáveis de ambiente

| Variável | Obrigatória | Valor padrão / exemplo | Observação |
|---|---:|---|---|
| `DB_HOST` | Sim | `localhost` | No Docker, o backend usa `postgres` |
| `DB_PORT` | Sim | `5000` | No Docker, o backend usa `5432` |
| `DB_NAME` | Sim | `aspmanager` | Nome do banco da aplicação |
| `DB_USER` | Sim | `aspmanager-user` | Usuário do banco da aplicação |
| `DB_PASSWORD` | Sim | `your_db_password_here` | Senha do banco da aplicação |
| `JWT_SECRET` | Sim | `your_jwt_secret_key_here` | Segredo usado para assinar JWTs |
| `CORS_ALLOWED_ORIGINS` | Não | `http://localhost:4200` (fallback) / `http://localhost:80` (exemplo) | Origens permitidas para CORS |
| `POSTGRES_DB` | Sim* | `aspmanager` | Usada apenas no Docker para o container do PostgreSQL |
| `POSTGRES_USER` | Sim* | `aspmanager-user` | Usada apenas no Docker para o container do PostgreSQL |
| `POSTGRES_PASSWORD` | Sim* | `your_postgres_password_here` | Usada apenas no Docker para o container do PostgreSQL |

\* Necessárias somente ao subir com `docker compose`.

## Swagger / OpenAPI

- Local: `http://localhost:8080/swagger-ui/index.html`
- Docker Compose: `http://localhost:9090/swagger-ui/index.html`

Rotas protegidas exigem **Bearer JWT**. No Swagger UI, use:

```
Bearer <seu_token_jwt>
```
