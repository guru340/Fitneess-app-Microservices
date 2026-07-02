# 🏋️ Fitness App Microservices

A learning-friendly **Fitness Management System** built using **Spring Boot Microservices** and **Spring Cloud**. The project demonstrates enterprise-level backend development by decomposing business functionalities into independent, scalable, and maintainable microservices.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Cloud
- Spring Cloud Config Server
- Eureka Service Discovery
- Spring Cloud Gateway
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Docker
- REST APIs

---

## 📌 Features

- Microservices Architecture
- API Gateway
- Eureka Service Discovery
- Centralized Configuration
- RESTful APIs
- Modular & Scalable Design
- Database Integration with MySQL
- Independent Service Deployment
- Fault Isolation
- Cloud-Native Architecture

---

## 📂 Project Structure

```text
Fitness-App-Microservices/
│
├── api-gateway/           # API Gateway
├── config-server/         # Centralized Configuration
├── discovery-server/      # Eureka Server
├── user-service/          # User Management
├── workout-service/       # Workout Management
├── nutrition-service/     # Nutrition Management
└── ...
```

---

## ⚙️ How It Works

1. Client sends requests to the **API Gateway**.
2. The Gateway routes requests to the appropriate microservice.
3. Services register themselves with **Eureka Server**.
4. Configuration is loaded from the **Config Server**.
5. Each microservice handles its own business logic and database.
6. The response is returned to the client through the Gateway.

---

## 🛠️ Getting Started

### Clone the Repository

```bash
git clone https://github.com/guru340/Fitneess-app-Microservices.git

cd Fitneess-app-Microservices
```

---

### Run the Services

Start the services in the following order:

1. Config Server

```bash
cd config-server
mvn spring-boot:run
```

2. Eureka Server

```bash
cd discovery-server
mvn spring-boot:run
```

3. API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

4. Start all remaining microservices

```bash
cd service-name
mvn spring-boot:run
```

---

## 🌐 Service URLs

| Service | Default Port |
|---------|--------------|
| Config Server | 8888 |
| Eureka Server | 8761 |
| API Gateway | 8080 |

---

## 📖 Learning Path

Explore the project in the following order:

1. Config Server
2. Eureka Server
3. API Gateway
4. Entity Classes
5. Repositories
6. Services
7. Controllers
8. Configuration Files
9. REST APIs

---


```

---

## 📦 Build

```bash
mvn clean install
```

---

## 🧪 Run Tests

```bash
mvn test
```

---

## 🐳 Docker

Build and run the application:

```bash
docker compose up --build
```

Stop the containers:

```bash
docker compose down
```

---

## 🚀 Future Enhancements

- JWT Authentication
- Spring Security
- Kafka Integration
- Redis Caching
- Resilience4j Circuit Breaker
- Prometheus & Grafana Monitoring
- Kubernetes Deployment
- GitHub Actions CI/CD
- AWS Deployment

---

## 👨‍💻 Built By

**Mayank Sangwani**

⭐ If you found this project helpful, consider giving it a star!
