
# card-details (Java 21 • Spring Boot 3.5.x • Maven multi-module)

This project demonstrates a Spring Boot WAR that **consumes two internal JARs** (Service and DAO), is packaged into an **EAR** for traditional app servers, and exposes all endpoints as **MCP tools** for agentic orchestration (e.g. CrewAI).

## Modules
- **card-credit-details-dao-jar**: Domain models + DAO (`@Repository`) with in-memory seeded data simulating downstream APIs.
- **card-credit-details-service-jar**: Business services (`@Service`) that orchestrate DAO calls, compute summary metrics, and produce agentic decision DTOs.
- **card-credit-details-war**: Spring Boot WAR exposing REST + MCP tool endpoints.
- **card-credit-details-ear**: EAR that bundles the WAR + JARs.

## Build
```bash
mvn clean install
```

Artifacts:
- DAO JAR: `card-credit-details-dao-jar/target/*.jar`
- Service JAR: `card-credit-details-service-jar/target/*.jar`
- WAR: `card-credit-details-war/target/card-credit-details.war`
- EAR: `card-credit-details-ear/target/card-credit-details-ear-1.0.0.ear`

## Run (dev)
Run the WAR with embedded Tomcat:
```bash
mvn spring-boot:run -pl card-credit-details-war
```

## Try it
Use the seeded demo card number: **123**

```
GET http://localhost:8080/card-details/appStatus
GET http://localhost:8080/card-details/123/summary
GET http://localhost:8080/card-details/123/status
GET http://localhost:8080/card-details/123/limits
GET http://localhost:8080/card-details/123/balance
GET http://localhost:8080/card-details/123/rewards
GET http://localhost:8080/card-details/123/transactions?lastN=5
GET http://localhost:8080/card-details/123/statements?lastN=2
GET http://localhost:8080/card-details/123/health
GET http://localhost:8080/card-details/123/high-value-transactions?threshold=1000&lastN=5
GET http://localhost:8080/card-details/123/next-best-action
```

## MCP Tool Server
The app also runs as an MCP server over SSE:
- SSE connect: `GET http://localhost:8080/sse`
- MCP message: `POST http://localhost:8080/mcp/message`

## Java version
This project uses **Java 21** and **Spring Boot 3.5.0**.
"# card-details-agentic" 
