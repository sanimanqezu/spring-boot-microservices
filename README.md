# Spring Cloud Microservices

[![CI](https://github.com/sanimanqezu/spring-boot-microservices/actions/workflows/ci.yml/badge.svg)](https://github.com/sanimanqezu/spring-boot-microservices/actions/workflows/ci.yml)

A distributed backend built on the full **Spring Cloud** stack, demonstrating service discovery, centralised configuration, API gateway routing, and independent business services — each with its own responsibility and database, wired together and runnable with a single `docker-compose up`.

## Architecture

```
                         ┌──────────────────┐
                         │  Config Server   │  centralised configuration
                         │      :7000        │
                         └────────┬─────────┘
                                  │
     ┌────────────────────────────┼────────────────────────────┐
     │                            │                            │
┌────▼─────┐   ┌──────────┐  ┌────▼─────┐  ┌──────────┐  ┌──────▼────┐
│  Users   │   │ Products │  │  Orders  │  │Addresses │  │  Eureka   │
│ service  │   │ service  │  │ service  │  │ service  │  │ Registry  │
└────┬─────┘   └────┬─────┘  └────┬─────┘  └────┬─────┘  │   :8761   │
     │              │             │             │        └───────────┘
     └──────────────┴──────┬──────┴─────────────┘
                           │  all services register with Eureka
                    ┌──────▼───────┐
                    │ API Gateway  │  single entry point
                    └──────┬───────┘
                           ▼
                        Clients
```

## Services

| Service | Role |
|---|---|
| **Eureka Server** (`:8761`) | Service registry — every service registers and discovers others by name |
| **Config Server** (`:7000`) | Centralised, externalised configuration for all services |
| **API Gateway** | Single entry point; routes and load-balances requests to services via Eureka |
| **Users** | User domain — CRUD and business logic, layered architecture |
| **Products** | Product catalogue domain |
| **Orders** | Order domain — calls Products and Users over OpenFeign, guarded by a Resilience4j circuit breaker with a fallback |
| **Addresses** | Address domain |
| **InventoryGUI** | Angular 17 frontend that talks to the stack through the gateway |

Each business service follows a clean layered structure — controllers → service interfaces → implementations → persistence — with a global exception handler and typed error responses.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 3.5.16, Spring Cloud 2025.0.0 (Netflix Eureka, Config Server, Gateway) |
| Inter-service calls | Spring Cloud OpenFeign + Resilience4j circuit breaker |
| API contract | Contract-first — controllers implement OpenAPI interfaces generated from each service's `api.yml` |
| Mapping | MapStruct (entity ↔ DTO) |
| Persistence | Spring Data JPA, PostgreSQL |
| Frontend | Angular 17 |
| Testing | JUnit 5, Mockito, AssertJ, Spring MockMvc slice tests |
| Build | Maven (multi-module) |
| Runtime | Docker Compose |

## Key design points

- **Contract-first APIs** — each service ships an OpenAPI spec (`src/main/resources/api.yml`); the Swagger codegen plugin generates the API interface and DTOs at build time, and the controllers implement those interfaces. The HTTP contract can't drift from the code.
- **Service discovery, not hard-coded URLs** — every service registers with Eureka and resolves peers by name. Feign clients in Orders (`@FeignClient`) call `products-service` / `users-service` without knowing where they run.
- **Resilient inter-service calls** — `OrdersServiceImpl.addOrder` is wrapped in a Resilience4j `@CircuitBreaker` with a fallback, so a Products/Users outage degrades gracefully instead of cascading.
- **Externalised configuration** — the Config Server serves shared configuration to every service, so environment differences live in one place.
- **Centralised database** — all services point at the shared portfolio PostgreSQL instance via `DB_HOST`/`DB_NAME` env vars, each with its own database.

## Running locally

**Prerequisites:** Java 25+, Maven, Node.js 22+ (frontend), Docker.

This stack uses the shared portfolio PostgreSQL and Docker network, so start those once from the portfolio root first:

```bash
# from the portfolio root (creates the central Postgres + portfolio-net)
docker-compose up -d
```

Then bring up the microservices:

```bash
docker-compose up --build
```

This starts the Eureka registry, the Config Server, all business services, and the API Gateway, all joined to `portfolio-net`. Once up:

- Eureka dashboard: `http://localhost:8761` — watch each service register
- API Gateway: `http://localhost:7002` — all API traffic goes through it, and it resolves service locations from Eureka

## Why this project

Built to learn distributed-systems fundamentals hands-on: how services discover each other without hard-coded URLs, how configuration is externalised and shared, how a gateway becomes the single front door, and how to keep each domain independently deployable.

## Tests

```bash
# per service, e.g.
cd services/Orders && ./mvnw test
```

Each business service is tested at two layers (80 tests in total):

| Layer | Scope |
|---|---|
| Service (`@ExtendWith(MockitoExtension)`) | Business rules against a mocked repository — happy paths, validation, not-found cases, and for Orders the Feign-dependent flows (user lookup, product existence, insufficient stock) |
| Controller (`@WebMvcTest`) | HTTP contract with a mocked service — correct status codes (`201`/`200`), request delegation, missing required params → `400`, and the `@ControllerAdvice` mapping of domain exceptions → `404` |

The controller slice tests are what prove the global exception handler actually translates a thrown `…NotFoundException` into a `404` with a typed error body — something the service-layer tests can't see.

## Project structure

```
spring-boot-microservices/
├── services/
│   ├── EurekaServer/       service registry
│   ├── ConfigServer/       centralised configuration
│   ├── GatewayService/     API gateway
│   ├── Users/              user domain
│   ├── Products/           product catalogue
│   ├── Orders/             order domain (Feign + circuit breaker)
│   ├── Addresses/          address domain
│   └── InventoryGUI/       Angular 17 frontend
└── docker-compose.yml      full stack orchestration
```
