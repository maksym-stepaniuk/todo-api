# Todo API

A simple RESTful Todo application built with **Spring Boot**.  
The project is intentionally built step-by-step to learn and practice:

- Modern Java
- Spring Boot fundamentals
- REST API design
- DTOs & Validation
- Global exception handling
- Filtering / searching / sorting via query params
- Testing Spring controllers with MockMvc

---

## Tech Stack

- Java 21
- Spring Boot 3
- Maven
- Spring Web
- Spring Validation
- JUnit 5 / MockMvc

---

## Project Structure

The application follows a layered architecture:

- `controller` → REST controllers (API layer)
- `service` → business logic
- `dto` → request/response objects
- `model` → domain models
- `exception` → custom exceptions + global error handling

---

## Getting Started

### Requirements

- Java **21+**
- Maven

### Run the application

```bash
mvn spring-boot:run
```

The server starts at:

- http://localhost:8080

---

## Health Check

**Request:**
```http
GET /health
```

**Response:**
```text
ok
```

---

## Tasks API

### Create Task

**Request:**
```http
POST /tasks
```

**Body:**
```json
{
  "title": "Learn Spring",
  "description": "Practice Day 7",
  "priority": 1,
  "dueAt": "2025-12-31T10:00:00Z"
}
```

**Responses:**

| Code | Meaning           |
|------|-------------------|
| 201  | created           |
| 400  | validation failed |

---

### Get All Tasks (with filters)

**Request:**
```http
GET /tasks
```

Supports:

| Param  | Description                 | Example          |
|--------|-----------------------------|------------------|
| status | filter by status            | TODO             |
| query  | search in title/description | spring           |
| sort   | sort by field               | priority, dueAt  |

Examples:

```bash
GET /tasks?status=TODO
GET /tasks?query=learn
GET /tasks?sort=priority
GET /tasks?status=DONE&query=test&sort=dueAt
```

---

### Get Task By ID

**Request:**
```http
GET /tasks/{id}
```

**Responses:**

| Code | Meaning   |
|------|-----------|
| 200  | ok        |
| 404  | not found |

---

### Update Task

**Request:**
```http
PUT /tasks/{id}
```

**Body:**
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "priority": 2,
  "dueAt": "2025-12-31T10:00:00Z"
}
```

---

### Change Task Status

**Request:**
```http
PATCH /tasks/{id}/status
```

**Body:**
```json
{
  "status": "DONE"
}
```

---

### Delete Task

**Request:**
```http
DELETE /tasks/{id}
```

**Responses:**

| Code | Meaning   |
|------|-----------|
| 204  | deleted   |
| 404  | not found |

---

## Validation

Example invalid request:

```json
{
  "title": "",
  "priority": null
}
```

Response `400`:

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

---

## Unified Error Format

All errors follow one structure:

```json
{
  "code": "TASK_NOT_FOUND",
  "message": "Task with id ... not found",
  "timestamp": "..."
}
```

Other codes include:

- `VALIDATION_ERROR`
- `INTERNAL_ERROR`

---

## Tests

API is covered with MockMvc tests including:

- ✔ create task
- ✔ invalid create → 400
- ✔ get not found → 404
- ✔ update
- ✔ delete → 204

Run tests:

```bash
mvn test
```

---

## Status

This project is intentionally built gradually to:

- understand Spring Boot internals
- practice clean API design

Future improvements may include:

- persistence (PostgreSQL + JPA)
- authentication
- pagination
- Docker
