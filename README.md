
# card-details

Java 21 + Spring Boot 3.5 multi-module application that serves card-account data via REST and MCP-style tool endpoints.

## Project Structure

- `card-credit-details-dao-jar`: in-memory data and domain models
- `card-credit-details-service-jar`: business logic, health scoring, next-best-action logic
- `card-credit-details-war`: Spring Boot web app with REST endpoints and workflow orchestration
- `card-credit-details-ear`: EAR packaging for traditional app-server deployment

## Architecture and Data Flow

The system follows a layered flow:

1. Request comes to controller in WAR module (`/card-details/...`).
2. Controller calls service layer in `card-credit-details-service-jar`.
3. Service layer calls DAO layer in `card-credit-details-dao-jar`.
4. DAO reads seeded in-memory data for card `123`.
5. Service computes derived fields (utilization, health status, recommendation).
6. Controller returns response as JSON (or text for the workflow endpoint).

### Workflow Endpoint Flow

`POST /card-details/agentic-query` runs three workflow nodes through an orchestrator:

1. `IntakeNode`: classifies intent from user question.
2. `AnalystNode`: pulls account health and relevant facts.
3. `ResolverNode`: builds user-facing answer and recommendation.

Advisors (`GuardrailsAdvisor`, `ValidatingAdvisor`) run before/after each node for validation and guardrails.

## Role of AI Agent and LLM

Java service is source of truth for business logic.
AI agent/LLM can orchestrate and explain when integrated externally.

## API Endpoints

### Base endpoints

- `GET /card-details/appStatus`
- `GET /card-details/{cardNo}/summary`
- `GET /card-details/{cardNo}/status`
- `GET /card-details/{cardNo}/limits`
- `GET /card-details/{cardNo}/balance`
- `GET /card-details/{cardNo}/rewards`
- `GET /card-details/{cardNo}/transactions?lastN=10`
- `GET /card-details/{cardNo}/statements?lastN=6`

### Composite endpoints

- `GET /card-details/{cardNo}/health`
- `GET /card-details/{cardNo}/high-value-transactions?threshold=1000&lastN=5`
- `GET /card-details/{cardNo}/next-best-action`

### Workflow endpoint

- `POST /card-details/agentic-query?cardNo=123&userRequest=Why%20is%20my%20credit%20low?`

## Windows Setup and Run

### Prerequisites

- Java 21 (`java -version`)
- Maven 3.9+ (`mvn -version`)
- Git (optional, for clone/push)

### Build

From repository root:

```bash
mvn clean install
```

### Run application

```bash
mvn spring-boot:run -pl card-credit-details-war
```

Application URL: `http://localhost:8080`

### Verify service

```bash
curl http://localhost:8080/card-details/appStatus
```

Expected response:

```text
OK
```

### Test seeded demo data

```bash
curl http://localhost:8080/card-details/123/summary
curl http://localhost:8080/card-details/123/health
curl http://localhost:8080/card-details/123/next-best-action
curl -X POST "http://localhost:8080/card-details/agentic-query?cardNo=123&userRequest=Why%20is%20my%20credit%20low?"
```

## Build Artifacts

After build, artifacts are generated under each module's `target` folder:

- DAO JAR
- Service JAR
- WAR
- EAR

`target` folders are build outputs and are excluded from version control via `.gitignore`.
