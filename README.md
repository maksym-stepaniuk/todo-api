# Todo API

RESTful Todo API on **Spring Boot** with PostgreSQL, Flyway, JWT auth, and integration tests.

---

## Tech Stack
- Java 21
- Spring Boot 3 (Web, Validation, Data JPA, Security, OAuth2 Resource Server)
- Maven (wrapper `./mvnw`)
- PostgreSQL + Flyway
- JUnit 5 / MockMvc / Testcontainers

---

## Project Structure
- `controller` — REST controllers
- `service` — business logic
- `repository` — Spring Data JPA repositories
- `dto` — request/response objects
- `entity` — JPA entities
- `model` — domain models/enums
- `security` — JWT, current user, security handlers
- `exception` — custom errors and global handler
- `config/DataInitializer` — seeds a default user and project

---

## Getting Started
### Requirements
- Java 21+
- Docker (for local DB and Testcontainers)
- Maven wrapper (`./mvnw`) is included

### Run the application (with Postgres)
```bash
docker compose up -d postgres
./mvnw spring-boot:run
```
Service listens on `http://localhost:8080`.

**Default seed:**
- user: `test@test.com`
- password: `change-me`
- project: `Default Project`

JWT secret is configured in `src/main/resources/application.yml` (`app.jwt.secret`).

---

## Auth (JWT)
### Register
```
POST /auth/register
{
  "email": "user@test.com",
  "password": "Password123!"
}
```

### Login
```
POST /auth/login
{
  "email": "user@test.com",
  "password": "Password123!"
}
```
Response: `accessToken`, `tokenType`, `expiresIn`.

Use the token for protected endpoints:
```
Authorization: Bearer <token>
```

---

## Projects
### Create project
```
POST /projects
Authorization: Bearer <token>
{
  "name": "My Project"
}
```

---

## Tasks (CRUD)
### Create task (requires projectId)
```
POST /tasks
Authorization: Bearer <token>
{
  "projectId": "<uuid>",
  "title": "Learn Spring",
  "description": "Practice",
  "priority": 1,
  "dueAt": "2026-12-31T10:00:00Z"
}
```

### Get / Update / Patch status / Delete
```
GET    /tasks/{id}
PUT    /tasks/{id}
PATCH  /tasks/{id}/status
DELETE /tasks/{id}
```

---

## Project Tasks (paged + filters)
```
GET /projects/{projectId}/tasks?status=TODO,IN_PROGRESS&priority=1,2&dueFrom=2026-01-01T00:00:00Z&dueTo=2026-12-31T23:59:59Z&q=deploy&page=0&size=10&sort=createdAt,desc
```
Returns `PageResponse<TaskResponse>` with `content`, `page`, `size`, `totalElements`, `totalPages`, `last`.

Filters supported:
- `status` (multiple)
- `priority` (multiple)
- `dueFrom`, `dueTo` (ISO `Instant`)
- `q` (title/description search)

---

## Admin
Endpoints (ADMIN only):
```
GET   /admin/users
PATCH /admin/users/{id}/role
{
  "admin": true
}
```

Roles are stored in `user_roles` and added to JWT as `roles` claim.

---

## Error Format
All errors use a unified JSON response:
```json
{
  "timestamp": "2026-01-31T11:51:45.777050100Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/tasks",
  "details": ["title: must not be blank"]
}
```

---

## API Demo (Postman)
A ready-to-run Postman collection is included:
- `postman_collection.json`

Import it in Postman and run requests top-to-bottom.

---

## Tests
Integration tests use Testcontainers (Docker required):
```bash
./mvnw test
```

---

## Status
The API supports JWT auth, role-based access, projects, task CRUD, and paged task listing with filters and search.
