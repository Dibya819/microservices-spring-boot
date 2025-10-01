🚦 Smart Traffic Management System 🚦

A microservices-based traffic management system built using Spring Boot, Spring Cloud, Kafka, RabbitMQ, JWT, and Resilience4j. The system manages traffic signals, vehicles, violations, user notifications, and analytics for a smarter, safer city.

🔥 Key Features

User Service

Manage drivers, traffic officers, and admins

JWT authentication and role-based access

CRUD operations with input validation

Vehicle Service

Vehicle registration and management

Owner details, vehicle types

Secure REST APIs with JWT

Eureka service registration

Traffic Signal Service

Track signals (red/green/yellow)

Periodic updates

Async communication with Kafka/RabbitMQ

Violation Service

Record traffic violations (speeding, signal jumping)

Sync & async communication with user/vehicle services

Resilience4j circuit breaker to handle service failures

Notification Service

Send notifications via Email/SMS

Event-driven async using Kafka/RabbitMQ

Analytics & Prediction Service

Traffic congestion prediction

Pollution monitoring

Scheduled jobs or async requests from other services

Gateway Service

API Gateway for routing, security, and load balancing

JWT token validation

Spring Cloud Load Balancer integration

Eureka Service

Service discovery for all microservices

Config Service

Centralized configuration for all services

📦 Tech Stack

Backend: Java, Spring Boot, Spring Cloud (Eureka, Gateway, Config)

Security: Spring Security, JWT

Database: MySQL/PostgreSQL

Messaging: Kafka / RabbitMQ

Resilience: Resilience4j (Circuit Breaker, Retry, Rate Limiter)

DevOps: Docker, Jenkins, AWS (EC2, S3), Kubernetes (planned)

Testing: JUnit, Mockito

API Documentation: Swagger UI

Monitoring: Actuator, Logs

🔗 Architecture Overview
graph TD
    A[User Service] -->|async/sync| B[Vehicle Service]
    B -->|async| C[Violation Service]
    C --> D[Notification Service]
    C --> E[Analytics Service]
    F[Traffic Signal Service] -->|async| C
    G[Gateway Service] -->|routes| A & B & C & F & E
    H[Eureka Service] -->|service discovery| A & B & C & F & E

🚀 Project Flow

User registration/login → JWT issued

Vehicle registration → Vehicle info stored

Traffic signals → Status updated periodically

Violation recorded → User & vehicle info fetched, notifications sent

Analytics service → Predict congestion and monitor pollution

Notifications → Sent asynchronously via Kafka/RabbitMQ

💻 Running the Project

Clone the repository:

git clone https://github.com/YourUsername/SmartTrafficSystem.git


Start Eureka Server

Start all microservices: User, Vehicle, Traffic Signal, Violation, Notification, Analytics, Gateway

Access APIs via Gateway at http://localhost:8080

Explore APIs with Swagger UI:

http://localhost:8080/swagger-ui.html


Run Kafka/RabbitMQ for async communication

🎯 Highlights

Fully microservices-based architecture

JWT-based security with role management

Async messaging for scalable communication

Circuit breaker & fault tolerance using Resilience4j

Centralized config & service discovery

Modular, extendable, production-ready architecture

📂 Folder Structure
smart-traffic-system/
├── user-service
├── vehicle-service
├── traffic-signal-service
├── violation-service
├── notification-service
├── analytics-service
├── gateway-service
├── eureka-service
└── config-service

🏆 Future Enhancements

Integrate AI-powered traffic predictions

Deploy fully on AWS using Kubernetes

Add mobile app integration for real-time traffic updates

Enhance analytics dashboard with charts and maps

👨‍💻 Author

Dibya Bikash Pradhan

Backend Developer | Java | Spring Boot | Microservices | DevOps

LinkedIn: Dibya Bikash Pradhan

GitHub: Dibya819

⭐ Make Your Repo Stand Out

Add Screenshots/GIFs of Swagger UI, dashboard, or Kafka flows

Include badges (Build status, Maven, License, Coverage)
Example:

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)


Add Postman collection for easy API testing

Add a UML diagram for services & entity relationships

Highlight contributions if open for collaboration
