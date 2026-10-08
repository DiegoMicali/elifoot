# ⚽ EliFoot API

REST API for managing football clubs — stadiums, clubs and players — built with **Java 17 + Spring Boot 3**, **OAuth2 / JWT (RS256)** authentication and fine-grained **scope-based** authorization.

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.13-6DB33F?logo=springboot&logoColor=white">
  <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white">
</p>

---

## 📋 About

EliFoot exposes a full CRUD over a football domain with real relationships (`Stadium 1:1 Club 1:N Player`), pagination, payload validation, DTO ↔ entity mapping via MapStruct and schema versioning with Flyway.

The security layer implements its own **Authorization Server + Resource Server**: login issues a JWT signed with an RSA private key, and every endpoint is guarded by **custom annotations** that translate scopes into access rules.

---

## 🛠 Tech stack

| Category | Stack |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.13 (Web, Data JPA, Validation) |
| Security | Spring Security · OAuth2 Resource Server · JWT RS256 (Nimbus) · BCrypt |
| Persistence | PostgreSQL · Hibernate · Flyway |
| Mapping | MapStruct · Lombok |
| Testing | JUnit 5 · Mockito · Testcontainers · Spring Security Test |
| Build | Maven (wrapper included) |

---

## 🏗 Architecture

```
src/main/java/dev/java10x/elifoot
├── config
│   ├── CorsConfig.java
│   └── security
│       ├── SecurityConfig.java          # filter chain, JWT encoder/decoder, BCrypt
│       └── annotation/{club,player,stadium}
│           └── Can{Read,Write}*.java    # custom authorization annotations
├── controller
│   ├── request/                         # inbound DTOs (+ Bean Validation)
│   └── response/                        # outbound DTOs
├── entity                               # Stadium, Club, Player, User, Scope, Position
├── exceptions                           # ResourceNotFound, ResourceAlreadyExists
├── mapper                               # MapStruct
├── repository                           # Spring Data JPA
└── service                              # Find* / Create* / LoginService
```

**Data model**

```
stadium 1──1 club 1──N player
users N──N scopes  (through users_scopes)
```

---

## 🔐 Security

**Flow:** `POST /login` checks the credentials with BCrypt, loads the user's scopes and issues an **RS256** JWT (claims `sub`, `email`, `scope`) valid for **600s**. Subsequent requests send `Authorization: Bearer <token>` and are validated by the Resource Server using the public key.

Sessions are **stateless** and CSRF is disabled — the standard setup for token-based APIs.

**Available scopes**

| Scope | Grants |
|---|---|
| `admin:all` | full access to every resource |
| `stadium:read` / `stadium:write` | list / create stadiums |
| `club:read` / `club:write` | list / create clubs |
| `player:read` / `player:write` | list / create players |

Rules are encapsulated in annotations, keeping controllers clean:

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('SCOPE_admin:all', 'SCOPE_club:write')")
public @interface CanWriteClub { }
```

---

## 🚀 Getting started

### Prerequisites
- JDK 17+
- Docker (or a local PostgreSQL)

### 1. Database

```bash
docker run --name elifoot-db -e POSTGRES_DB=elifoot -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

### 2. RSA keys

The keys are **not versioned**. Generate the pair under `src/main/resources`:

```bash
openssl genrsa -out src/main/resources/authz.pem 2048 && openssl rsa -in src/main/resources/authz.pem -pubout -out src/main/resources/authz.pub
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Flyway creates the schema automatically. The API starts at `http://localhost:8080`.

### 4. Seed the scopes

```sql
INSERT INTO scopes (name) VALUES
  ('admin:all'), ('stadium:read'), ('stadium:write'),
  ('club:read'), ('club:write'), ('player:read'), ('player:write');
```

---

## 📡 Endpoints

| Method | Route | Required scope |
|---|---|---|
| `POST` | `/users` | public |
| `POST` | `/login` | public |
| `GET` | `/resources/positions` | authenticated |
| `GET` | `/stadiums` | `stadium:read` |
| `POST` | `/stadiums` | `stadium:write` |
| `GET` | `/clubs` | `club:read` |
| `GET` | `/clubs/{id}` | `club:read` |
| `POST` | `/clubs` | `club:write` |
| `GET` | `/clubs/{id}/players` | `club:write` |
| `GET` | `/players` | `player:read` |
| `GET` | `/players/{id}` | `player:read` |
| `POST` | `/players` | `player:write` |

> List endpoints support Spring Data pagination: `?page=0&size=10&sort=name,asc`.

### Usage example

**Create a user**

```bash
curl -X POST http://localhost:8080/users -H 'Content-Type: application/json' -d '{"name":"Diego","email":"diego@elifoot.dev","password":"123456","scopes":[1]}'
```

**Authenticate**

```bash
curl -X POST http://localhost:8080/login -H 'Content-Type: application/json' -d '{"email":"diego@elifoot.dev","password":"123456"}'
```

```json
{ "accessToken": "eyJhbGciOiJSUzI1NiJ9...", "expiresIn": 600 }
```

**Create a stadium**

```bash
curl -X POST http://localhost:8080/stadiums -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"name":"Maracanã","city":"Rio de Janeiro","capacity":78838}'
```

---

## 🧪 Tests

```bash
./mvnw test
```

Testing strategy for the project:

- **Unit** — services in isolation with JUnit 5 + Mockito;
- **Integration** — full HTTP cycle with `MockMvc`, `spring-security-test` and a real PostgreSQL through **Testcontainers**;
- **Coverage** — JaCoCo report at `target/site/jacoco/index.html`.

---

## 🗺 Roadmap

- [x] Project setup, entities and migrations
- [x] Stadium, Club and Player resources
- [x] DTOs with MapStruct and pagination
- [x] OAuth2 + JWT, login and user sign-up
- [x] Scope-based authorization with custom annotations
- [x] Global exception handler (`@ControllerAdvice`)
- [ ] Unit tests with Mockito
- [ ] Integration tests with Testcontainers
- [ ] Coverage report with JaCoCo

---

## 👤 Author

**Diego Micali** — [GitHub](https://github.com/DiegoMicali)

Built as part of the **Java10x** course.
