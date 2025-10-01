<<<<<<< HEAD
# 🚦 Smart Traffic Management System

![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot)
![Docker](https://img.shields.io/badge/Docker-ready-blue?logo=docker)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A **microservices-based traffic management system** built using **Spring Boot, Spring Cloud, Kafka, RabbitMQ, JWT, and Resilience4j**.  
It manages **users, vehicles, traffic signals, violations, notifications, and analytics** for smarter, safer cities.

---

## 📌 Microservices Overview

| Service | Responsibilities |
|---------|-----------------|
| **User Service** | Manage drivers, traffic officers, admins; JWT auth, CRUD, validation |
| **Vehicle Service** | Vehicle registration, owner info, REST API |
| **Traffic Signal Service** | Track signals (red/yellow/green); periodic updates; async messaging |
| **Violation Service** | Record traffic violations; fetch user/vehicle info; Resilience4j fault tolerance |
| **Notification Service** | Send notifications via Email/SMS; async events (Kafka/RabbitMQ) |
| **Analytics & Prediction Service** | Traffic congestion prediction; pollution monitoring |
| **Gateway Service** | API routing, JWT validation, load balancing |
| **Eureka Service** | Service discovery for all microservices |
| **Config Service (optional)** | Centralized configuration for all services |

---

## 🏗️ Tech Stack

| Category       | Technologies |
|----------------|-------------|
| Language       | Java 17 |
| Framework      | Spring Boot, Spring Cloud |
| Messaging      | Kafka, RabbitMQ |
| Security       | Spring Security, JWT |
| Resilience     | Resilience4j (Circuit Breaker, Retry, Rate Limiter) |
| Databases      | MySQL / PostgreSQL |
| Deployment     | Docker, Kubernetes (future), AWS (future) |
| Documentation  | Swagger UI |

---

## 📂 Project Structure

microservices-spring-boot/
├── user-service
├── vehicle-service
├── traffic-signal-service
├── violation-service
├── notification-service
├── analytics-service
├── gateway-service
├── eureka-service
└── config-service

yaml
Copy code

---

## 🚀 How to Run Locally

1. Clone the repo:
```bash
git clone https://github.com/Dibya819/microservices-spring-boot.git
cd microservices-spring-boot
Start Eureka Service first.

Start each microservice in this order:

User Service

Vehicle Service

Traffic Signal Service

Violation Service

Notification Service

Analytics Service

Gateway Service

Access APIs via Gateway:

arduino
Copy code
http://localhost:8080
Open Swagger UI:

bash
Copy code
http://localhost:8080/swagger-ui.html
🔄 System Communication Flow (Coming Soon)
📌 System flow diagram showing sync and async communication will be added here.

📸 Screenshots (Coming Soon)
📌 Screenshots of Swagger UI, Eureka Dashboard, and Kafka events will be added here.

📅 Roadmap / Future Enhancements
 User Service (JWT Auth, CRUD)

 Vehicle Service

 Traffic Signal Service with Kafka

 Violation Service with Resilience4j

 Notification Service with email/SMS

 Analytics & Prediction Service

 Deploy on AWS with Kubernetes

 Real-time dashboard UI

 AI-based traffic prediction

 ## 👤 Author
**Dibya Bikash Pradhan**  
- GitHub: [Dibya819](https://github.com/Dibya819)  
- LinkedIn: [LinkedIn](https://www.linkedin.com/in/dibya-bikash-pradhan)
=======
>>>>>>> d75a2bf93e0b6c1fbe9a59b3b620a5f7956917ed

