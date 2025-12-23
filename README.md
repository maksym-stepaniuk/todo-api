# Todo API

A simple RESTful Todo application built with **Spring Boot**.

The project is designed as a learning and portfolio project to practice:
- Java (modern versions)
- Spring Boot fundamentals
- REST API design
- Validation and error handling
- Testing Spring applications

---

## Tech Stack

- Java 21
- Spring Boot
- Maven
- Spring Web
- Spring Validation
- JUnit 5 / MockMvc (later)

---

## Project Structure

The application follows a layered architecture:

- **controller** - REST controllers (API layer)
- **service** - business logic
- **dto** - request/response objects
- **model** - domain models
- **exception** - custom exceptions and global error handling

---

## Getting Started

### Requirements
- Java 21+
- Maven

### Run the application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

---

## Health Check

```http
GET /health
```

Response:

```text
ok
```

---

## API (in progress)

This API will provide CRUD operations for managing tasks:

- Create task
- Get task(s)
- Update task
- Change task status
- Delete task

More details will be added as the project evolves.

---

## Notes

This project is intentionally built step by step.  
Each feature is added gradually to focus on understanding Spring Boot internals and clean API design.
