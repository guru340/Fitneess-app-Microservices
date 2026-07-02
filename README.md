# Fitness App Microservices

A production-oriented **Fitness Management System** built using **Spring Boot Microservices** and **Spring Cloud**, following modern cloud-native architecture principles. The project demonstrates how enterprise applications can be designed as independently deployable services with centralized configuration, service discovery, API gateway routing, and scalable RESTful APIs.

---

## Overview

This application adopts a **microservices architecture** to separate business functionalities into independent services, improving scalability, maintainability, and fault tolerance. Each service manages its own business logic while communicating through REST APIs under a centralized gateway.

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Cloud
- Spring Data JPA
- Hibernate
- Maven

### Cloud Components
- Spring Cloud Config Server
- Eureka Service Discovery
- Spring Cloud Gateway

### Database
- MySQL

### DevOps
- Docker
- Docker Compose

---

## Key Features

- Enterprise-grade Microservices Architecture
- Centralized Configuration Management
- Service Discovery using Eureka
- API Gateway for Request Routing
- Independent Deployable Services
- RESTful API Design
- Database Integration with JPA & Hibernate
- Modular and Maintainable Codebase
- Cloud-Native Development Practices
- Scalable System Design

---

## Project Structure

```text
Fitness-App-Microservices/
│
├── api-gateway/          # API Gateway
├── config-server/        # Centralized Configuration
├── discovery-server/     # Eureka Service Registry
├── user-service/         # User Management
├── workout-service/      # Workout Management
├── nutrition-service/    # Nutrition Management
└── ...
```

> Replace the above service names with the actual modules in your repository.

---

## System Architecture

```text
                   Client Applications
                           │
                           ▼
                    API Gateway
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
   User Service      Workout Service    Nutrition Service
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                        MySQL Database

                ▲
                │
         Config Server

                ▲
                │
        Eureka Discovery Server
```

---

## Request Flow

1. Client requests are received through the API Gateway.
2. The Gateway identifies the appropriate microservice.
3. Eureka Service Discovery locates the target service.
4. Configuration is fetched from the Config Server.
5. Business logic is processed within the selected microservice.
6. Data is persisted or retrieved from the database.
7. The response is returned to the client via the Gateway.

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/guru340/Fitneess-app-Microservices.git

cd Fitneess-app-Microservices
```

---

## Running the Application

Start the services in the following order:

### 1. Config Server

```bash
cd config-server
mvn spring-boot:run
```

### 2. Eureka Discovery Server

```bash
cd discovery-server
mvn spring-boot:run
```

### 3. API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

### 4. Start All Business Services

```bash
cd <service-name>
mvn spring-boot:run
```

---

## Default Ports

| Component | Port |
|-----------|------|
| Config Server | 8888 |
| Eureka Server | 8761 |
| API Gateway | 8080 |

---

## Build

```bash
mvn clean install
```

---

## Run Tests

```bash
mvn test
```

---

## Docker

Build and start all services:

```bash
docker compose up --build
```

Stop all running containers:

```bash
docker compose down
```

---

## Development Concepts Demonstrated

- Microservices Architecture
- API Gateway Pattern
- Service Discovery
- Centralized Configuration
- RESTful API Development
- Spring Boot Best Practices
- Database Persistence with JPA
- Dependency Injection
- Modular Application Design
- Cloud-Native Backend Development

---

## Future Enhancements

- JWT Authentication & Authorization
- Spring Security
- Kafka Event-Driven Communication
- Redis Caching
- Circuit Breaker (Resilience4j)
- Distributed Tracing
- Prometheus & Grafana Monitoring
- Kubernetes Deployment
- GitHub Actions CI/CD Pipeline
- AWS Cloud Deployment

---

## Author

**Mayank Sangwani**

- GitHub: https://github.com/guru340

---

If you find this project helpful, consider giving it a ⭐ on GitHub.
