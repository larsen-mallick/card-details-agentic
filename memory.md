# memory.md

## Session Context (as of 2026-03-22)

- **Project**: card-details (multi-module Maven project)
- **Modules**: card-credit-details-dao-jar, card-credit-details-service-jar, card-credit-details-war, card-credit-details-ear
- **OS**: Windows
- **Build Tool**: Maven
- **Spring Boot**: v3.5.0
- **Java**: 21.0.10
- **MCP Server**: Integration attempted via Postman and config JSON
- **API Endpoint (working)**: http://localhost:8080/card-details/123/balance
- **MCP Config (attempted)**:
  ```json
  {
      "mcpServers": {
          "card-details-server": {
              "version": "1.0.0",
              "transport": {
                  "type": "http",
                  "url": "http://localhost:8080/mcp/message"
              }
          }
      }
  }
  ```
- **Issues Faced**:
  - Build failures due to missing dependencies (resolved)
  - Connection failed when enabling MCP server via Postman
  - Unsure about correct MCP endpoint and Postman usage
- **Recent Actions**:
  - Build succeeded after dependency fix
  - Spring Boot app running, MCP server beans initialized
  - User requests to test MCP server endpoint (not REST), and to query total balance via MCP

---
This file is used to track the current session context, issues, and configuration for the card-details project.

