# Spring Cloud Microservices

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
| **Users** | User domain — CRUD and business logic (27 classes, layered architecture) |
| **Products** | Product catalogue domain (20 classes) |
| **Orders** | Order domain with global exception handling and validation (24 classes) |
| **Addresses** | Address domain (17 classes) |

Each business service follows a clean layered structure — controllers → service interfaces → implementations → persistence — with a global exception handler and typed error responses.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2, Spring Cloud 2023 (Netflix Eureka, Config Server, Gateway) |
| Persistence | Spring Data JPA, PostgreSQL |
| Build | Maven (multi-module) |
| Runtime | Docker Compose |

## Running locally

**Prerequisites:** Java 17+, Maven, Docker.

```bash
docker-compose up --build
```

This starts PostgreSQL, the Eureka registry, the Config Server, all business services, and the API Gateway. Once up:

- Eureka dashboard: `http://localhost:8761` — watch each service register
- All API traffic goes through the **API Gateway**, which resolves service locations from Eureka

## Why this project

Built to learn distributed-systems fundamentals hands-on: how services discover each other without hard-coded URLs, how configuration is externalised and shared, how a gateway becomes the single front door, and how to keep each domain independently deployable.

## Project structure

```
spring-boot-microservices/
├── services/
│   ├── EurekaServer/       service registry
│   ├── ConfigServer/       centralised configuration
│   ├── GatewayService/     API gateway
│   ├── Users/              user domain
│   ├── Products/           product catalogue
│   ├── Orders/             order domain
│   └── Addresses/          address domain
└── docker-compose.yml      full stack orchestration
```
