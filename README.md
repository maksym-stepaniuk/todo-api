# Todo API

RESTful Todo application on **Spring Boot** with PostgreSQL, Flyway, and integration tests.

---

## Tech Stack
- Java 21
- Spring Boot 3 (Web, Validation, Data JPA)
- Maven (wrapper `./mvnw`)
- PostgreSQL + Flyway
- JUnit 5 / MockMvc / Testcontainers

---

## Project Structure
- `controller` — REST controllers
- `service` — business logic
- `dto` — request/response objects
- `model` — domain models
- `exception` — custom errors and global handler
- `entity` — JPA entities
- `config/DataInitializer` — seeds a default user and project

---

## Getting Started
### Requirements
- Java 21+
- Docker (for tests and local DB)
- Maven wrapper (`./mvnw`) is included

### Run the application (with Postgres)
```bash
docker compose up -d postgres   # starts postgres:16 with creds from application.yml
./mvnw spring-boot:run
```
Service listens on `http://localhost:8080`. A default user `test@test.com` and project `Default project` are created by `DataInitializer`.

---

## Health Check
```
GET /health -> ok
```

---

## Tasks API (main)
### Create Task
```
POST /tasks
{
  "title": "Learn Spring",
  "description": "Practice",
  "priority": 1,
  "dueAt": "2025-12-31T10:00:00Z"
}
```
201 on success, 400 on validation errors.

### Get Tasks (in-memory filters)
```
GET /tasks?status=TODO&query=learn&sort=priority
```
Supports `status`, `query` (title/description), `sort` (`priority`|`dueAt`).

### Get / Update / Patch status / Delete
```
GET    /tasks/{id}
PUT    /tasks/{id}
PATCH  /tasks/{id}/status
DELETE /tasks/{id}
```

---

## Project Tasks (paged)
```
GET /projects/{projectId}/tasks?status=TODO&page=0&size=20
```
Returns `PageResponse<TaskResponse>` with `content`, `page`, `size`, `totalElements`, `totalPages`, `last`.

---

## Validation & Errors
Example 400 response:
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "details": {
    "title": "must not be blank",
    "priority": "must not be null"
  }
}
```
Error format is unified (`code`, `message`, `timestamp`). Possible codes: `VALIDATION_ERROR`, `TASK_NOT_FOUND`, `INTERNAL_ERROR`.

---

## Tests
Integration tests use Testcontainers (Docker required):
```bash
./mvnw test
```
Coverage:
- MockMvc tests for the task controller
- Repository/service integration against real Postgres in a container

---

## Status
Runs with PostgreSQL via JPA/Flyway, supports CRUD for tasks and paginated project tasks. Testcontainers ensure reproducible integration tests.
