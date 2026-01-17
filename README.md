# Modify Realtime Project
A microservices-based real-time healthcare system built with Spring Boot, featuring multiple services for order management, user management, and notifications with Kafka integration.

## Architecture

This project follows a microservices architecture with the following components:

- **Gateway Service**: API Gateway for routing requests to appropriate microservices
- **Order Management Service**: Handles order-related operations
- **User Management Service**: Manages user data and operations
- **Notification Service**: Processes and sends notifications using Kafka

## Technologies Used

- **Java** - Primary programming language
- **Spring Boot** - Framework for building microservices
- **Spring Security** - JWT based security to handle user permissions
- **Apache Kafka** - Message broker for real-time event streaming
- **Docker** - Containerization (docker-compose.yaml included)
- **REST APIs** - Communication between services

## Prerequisites

Before running this project, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- Apache Kafka (or use Docker Compose setup)

## Project Structure
```
modify-realtimeproject/
├── gateway/gateway/              # API Gateway service
├── order/ordermanagement/        # Order management microservice
├── usermanagement/usermanagement/ # User management microservice
├── notificationservice/notificationservice/ # Notification service
├── docker-compose.yaml           # Docker Compose configuration
├── MODIFY_API_COLLECTION         # API collection for testing
└── README.md
```

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/kmallinath/modify-realtimeproject.git
cd modify-realtimeproject
```

### 2. Build the Project
```bash
# Build all services
mvn clean install
```

### 3. Run with Docker Compose
```bash
docker-compose up -d
```

This will start required dependencies (Kafka, Zookeeper, etc.)

### 4. Running Individual Services

You can also run each service independently:
```bash
# Gateway Service
cd gateway/gateway
mvn spring-boot:run

# Order Management Service
cd order/ordermanagement
mvn spring-boot:run

# User Management Service
cd usermanagement/usermanagement
mvn spring-boot:run

# Notification Service
cd notificationservice/notificationservice
mvn spring-boot:run
```
### 5. Download Postgres and Create

You can also run each service independently:
```bash
Download Postgres and create database userdb(change in application yaml files of each service if other database name is used)
Below is the example config
spring:
  application:
    name: user-management
  datasource:
    url: jdbc:postgresql://localhost:5432/userdb ((AS PER THE USER DB NAME)
    username: postgres (AS PER THE USER)
    password: (AS PER THE USER)
    driver-class-name: org.postgresql.Driver
```

## API Testing

Import the `MODIFY_API_COLLECTION` file into Postman or any API client to test the available endpoints.

## Service Endpoints

- **Gateway**: `http://localhost:8070` 
- **Order Service**: `http://localhost:8081`
- **User Management**: `http://localhost:8080`
- **Notification Service**: `http://localhost:8082`


## Kafka Integration (No Action Needed)

The notification service uses Kafka for real-time message processing:

- **Topics**: Configure your Kafka topics in application.properties
- **Producers**: Order and User services publish events
- **Consumers**: Notification service consumes events and triggers notifications

## Configuration

Each service has its own `application.properties` or `application.yml` file located in `src/main/resources/`. Update these files to configure:

- Database connections
- Kafka broker URLs
- Service ports
- Other service-specific settings


## Troubleshooting

### Common Issues

1. **Kafka Connection Issues**: Ensure Kafka and Zookeeper are running before starting services
2. **Port Conflicts**: Check if configured ports are already in use
3. **Docker Issues**: Try `docker-compose down` and `docker-compose up --build`


---

**Note**: This is a learning/demo project showcasing microservices architecture with real-time notification capabilities using Spring Boot and Kafka.
