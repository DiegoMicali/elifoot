# ⚽ EliFoot API

API REST para gerenciamento de clubes de futebol — estádios, clubes e jogadores — construída com **Java 17 + Spring Boot 3**, autenticação **OAuth2 / JWT (RS256)** e autorização granular por **escopos**.

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.13-6DB33F?logo=springboot&logoColor=white">
  <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white">
</p>

---

## 📋 Sobre o projeto

O EliFoot expõe um CRUD completo de um domínio futebolístico com relacionamentos reais (`Stadium 1:1 Club 1:N Player`), paginação, validação de payloads, conversão DTO ↔ entidade via MapStruct e versionamento de schema com Flyway.

A camada de segurança implementa um **Authorization Server + Resource Server** próprios: o login emite um JWT assinado com chave RSA privada, e cada endpoint é protegido por **anotações customizadas** que traduzem escopos em regras de acesso.

---

## 🛠 Tecnologias

| Categoria | Stack |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5.13 (Web, Data JPA, Validation) |
| Segurança | Spring Security · OAuth2 Resource Server · JWT RS256 (Nimbus) · BCrypt |
| Persistência | PostgreSQL · Hibernate · Flyway |
| Mapeamento | MapStruct · Lombok |
| Testes | JUnit 5 · Mockito · Testcontainers · Spring Security Test |
| Build | Maven (wrapper incluso) |

---

## 🏗 Arquitetura

```
src/main/java/dev/java10x/elifoot
├── config
│   ├── CorsConfig.java
│   └── security
│       ├── SecurityConfig.java          # filter chain, JWT encoder/decoder, BCrypt
│       └── annotation/{club,player,stadium}
│           └── Can{Read,Write}*.java    # anotações customizadas de autorização
├── controller
│   ├── request/                         # DTOs de entrada (+ Bean Validation)
│   └── response/                        # DTOs de saída
├── entity                               # Stadium, Club, Player, User, Scope, Position
├── exceptions                           # ResourceNotFound, ResourceAlreadyExists
├── mapper                               # MapStruct
├── repository                           # Spring Data JPA
└── service                              # Find* / Create* / LoginService
```

**Modelo de dados**

```
stadium 1──1 club 1──N player
users N──N scopes  (via users_scopes)
```

---

## 🔐 Segurança

**Fluxo:** `POST /login` valida as credenciais com BCrypt, carrega os escopos do usuário e emite um JWT **RS256** (claims `sub`, `email`, `scope`), com validade de **600s**. As requisições seguintes enviam `Authorization: Bearer <token>` e são validadas pelo Resource Server com a chave pública.

A sessão é **stateless** e o CSRF está desabilitado — o padrão para APIs com token.

**Escopos disponíveis**

| Escopo | Permite |
|---|---|
| `admin:all` | acesso total a todos os recursos |
| `stadium:read` / `stadium:write` | listar / criar estádios |
| `club:read` / `club:write` | listar / criar clubes |
| `player:read` / `player:write` | listar / criar jogadores |

As regras ficam encapsuladas em anotações, mantendo os controllers limpos:

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('SCOPE_admin:all', 'SCOPE_club:write')")
public @interface CanWriteClub { }
```

---

## 🚀 Como executar

### Pré-requisitos
- JDK 17+
- Docker (ou um PostgreSQL local)

### 1. Banco de dados

```bash
docker run --name elifoot-db -e POSTGRES_DB=elifoot -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

### 2. Chaves RSA

As chaves **não são versionadas**. Gere o par em `src/main/resources`:

```bash
openssl genrsa -out src/main/resources/authz.pem 2048 && openssl rsa -in src/main/resources/authz.pem -pubout -out src/main/resources/authz.pub
```

### 3. Subir a aplicação

```bash
./mvnw spring-boot:run
```

O Flyway cria o schema automaticamente. A API sobe em `http://localhost:8080`.

### 4. Popular os escopos

```sql
INSERT INTO scopes (name) VALUES
  ('admin:all'), ('stadium:read'), ('stadium:write'),
  ('club:read'), ('club:write'), ('player:read'), ('player:write');
```

---

## 📡 Endpoints

| Método | Rota | Escopo exigido |
|---|---|---|
| `POST` | `/users` | público |
| `POST` | `/login` | público |
| `GET` | `/resources/positions` | autenticado |
| `GET` | `/stadiums` | `stadium:read` |
| `POST` | `/stadiums` | `stadium:write` |
| `GET` | `/clubs` | `club:read` |
| `GET` | `/clubs/{id}` | `club:read` |
| `POST` | `/clubs` | `club:write` |
| `GET` | `/clubs/{id}/players` | `club:write` |
| `GET` | `/players` | `player:read` |
| `GET` | `/players/{id}` | `player:read` |
| `POST` | `/players` | `player:write` |

> Endpoints de listagem aceitam paginação do Spring Data: `?page=0&size=10&sort=name,asc`.

### Exemplo de uso

**Criar usuário**

```bash
curl -X POST http://localhost:8080/users -H 'Content-Type: application/json' -d '{"name":"Diego","email":"diego@elifoot.dev","password":"123456","scopes":[1]}'
```

**Autenticar**

```bash
curl -X POST http://localhost:8080/login -H 'Content-Type: application/json' -d '{"email":"diego@elifoot.dev","password":"123456"}'
```

```json
{ "accessToken": "eyJhbGciOiJSUzI1NiJ9...", "expiresIn": 600 }
```

**Criar um estádio**

```bash
curl -X POST http://localhost:8080/stadiums -H 'Content-Type: application/json' -H "Authorization: Bearer $TOKEN" -d '{"name":"Maracanã","city":"Rio de Janeiro","capacity":78838}'
```

---

## 🧪 Testes

```bash
./mvnw test
```

Estratégia adotada no projeto:

- **Unitários** — services isolados com JUnit 5 + Mockito;
- **Integração** — ciclo HTTP completo com `MockMvc`, `spring-security-test` e PostgreSQL real via **Testcontainers**;
- **Cobertura** — relatório JaCoCo em `target/site/jacoco/index.html`.


