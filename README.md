# 🚦 Smart Traffic Management System

![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot)
![Docker](https://img.shields.io/badge/Docker-ready-blue?logo=docker)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A **microservices-based Smart Traffic Management System** built with **Spring Boot, Spring Cloud, Kafka, RabbitMQ, JWT, Razorpay, and Resilience4j**.  
It manages **users, vehicles, traffic signals, violations, notifications, analytics, and online payments** for smarter, safer cities.

---

## 📌 Microservices Overview

| Service | Responsibilities |
|----------|------------------|
| 🧍 **User Service** | Manage drivers, officers, admins; authentication using JWT; CRUD and validation |
| 🚗 **Vehicle Service** | Handle vehicle registration and ownership information via REST APIs |
| 🚦 **Traffic Signal Service** | Track traffic signals (Red/Yellow/Green); periodic updates; async communication (Kafka/RabbitMQ) |
| ⚠️ **Violation Service** | Record traffic violations; fetch vehicle/user data; uses Resilience4j for fault tolerance |
| 💳 **Payment Service** | Integrates **Razorpay API** for fine payments and transaction management |
| 📢 **Notification Service** | Send alerts and notifications (Email/SMS) via event-driven messaging |
| 📊 **Analytics & Prediction Service** | Analyze traffic data, predict congestion, and monitor pollution |
| 🌐 **Gateway Service** | API Gateway for routing, JWT validation, and load balancing |
| 🔍 **Eureka Service** | Service discovery and registration for all microservices |
| ⚙️ **Config Service (Optional)** | Centralized configuration management for all services |

---

## 🏗️ Tech Stack

| Category | Technologies |
|-----------|--------------|
| **Language** | Java 17 |
| **Frameworks** | Spring Boot, Spring Cloud |
| **Messaging** | Apache Kafka, RabbitMQ |
| **Security** | Spring Security, JWT |
| **Resilience** | Resilience4j (Circuit Breaker, Retry, Rate Limiter) |
| **Payment Gateway** | Razorpay Integration |
| **Databases** | MySQL / PostgreSQL |
| **Containerization** | Docker, Docker Compose |
| **Future Deployment** | Kubernetes, AWS (ECS, EKS, EC2, S3) |
| **API Docs** | Swagger UI / OpenAPI 3.0 |

---

## 📂 Project Structure

microservices-spring-boot/
├── user-service/
├── vehicle-service/
├── traffic-signal-service/
├── violation-service/
├── notification-service/
├── payment-service/
├── analytics-service/
├── gateway-service/
├── eureka-service/
└── config-service/



2️⃣ Start services in order

Eureka Service

User Service

Vehicle Service

Traffic Signal Service

Violation Service

Notification Service

Payment Service

Analytics Service

Gateway Service

3️⃣ Access the system

Gateway base URL:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui.html

🔄 Communication Flow (Coming Soon)

A detailed architecture diagram showing synchronous REST and asynchronous Kafka/RabbitMQ communication between microservices will be added soon.

📸 Screenshots (Coming Soon)

Swagger UI endpoints

Eureka Service Dashboard

Kafka and RabbitMQ event logs

Razorpay payment workflow

🧭 Roadmap / Future Enhancements

✅ JWT Authentication in User Service

✅ REST-based inter-service communication

✅ Razorpay Payment Integration

🔄 Kafka/RabbitMQ Event Messaging

🔄 Resilience4j Circuit Breakers in Violation Service

🔄 Email/SMS Notifications via Notification Service

🔄 Analytics & Prediction Service using ML models

⏳ Docker + Kubernetes Deployment

⏳ AWS Deployment (ECS, EKS, RDS)

⏳ Real-time dashboard UI

⏳ AI-powered traffic and pollution prediction

🧠 Key Features

Secure JWT Authentication

Microservice Communication via REST + Kafka

Razorpay-based Payment System

Centralized Service Discovery (Eureka)

Fault Tolerance (Resilience4j)

Dockerized Deployment

Scalable & Extensible Architecture

👤 Author

Dibya Bikash Pradhan
🚀 Java Developer | Spring Boot | Microservices | DevOps Enthusiast

🔗 GitHub: Dibya819

💼 LinkedIn: Dibya Bikash Pradhan

💡 “Smart cities need smart traffic systems — and this project is one step closer to that vision.”
