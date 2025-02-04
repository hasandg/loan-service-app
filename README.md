# Loan Management System

## Overview
Backend Loan API for bank employees to manage customer loans with secure authentication and advanced payment features.

## Technology Stack
- Java 23
- Spring Boot 3.3.4
- Swagger
- H2 Database
- Mapstruct
- JUnit 5
- Mockito

## Features
- Create loans with flexible configurations
- List customer loans and installments
- Pay loan installments with smart payment logic
- Reward/Penalty system for early/late payments

## Setup and Installation
1. Clone the repository
2. Ensure Java 23 is installed
3. Run `./mvnw clean install`
4. Start application: `./mvnw spring-boot:run`

## Endpoints
- `/loans/create`: Create new loan
- `/loans/list`: List loans
- `/loans/installments`: List loan installments
- `/loans/pay`: Pay loan installments

## Swagger
- Access Swagger UI at: `http://localhost:8089/swagger-ui.html`

## profiles like dev, prod, test

- use `./mvnw spring-boot:run -Dspring.profiles.active=dev` to run in dev profile
- USE `./mvnw spring-boot:run -Dspring.profiles.active=prod` to run in prod profile

## Testing
Run tests using: `./mvnw test`

## Future Improvements

- add liquibase or flyway for database migration
- Bonus 1 could be with using Spring Security User instead of custom User, following repos implemented this feature
- like this repo https://github.com/hasandg/Spring_Boot_Cloud_Security
- add Spring Cloud, Spring Security, Eureka Discovery Service, Spring Api Gateway,
- Load Balancer, Spring Cloud Config, Spring Cloud Bus, Feign Client, Resilience4j-Circuit Breaker,
- Distributed Tracing with Micrometer and Zipkin, Aggregating Log files with ELK Stack,
- Run Microservice with Docker Container features
- like this repo https://github.com/hasandg/Bank-Account-Project-with-CQRS-and-Event-Sourcing-Kafka
- add CQRS, Event Sourcing, DDD(Domain Driven Design), Kafka features

## Sample Requests
- Create Loan:
```json
{
  "customerId": 1,
  "loanAmount": 1000,
  "numberOfInstallments": 12,
  "interestRate": 0.5
}
```
- List Loans:
```json
{
  "customerId": 1
}
```
```json
{
  "customerId": 1,
  "isPaid": false
}
```
```json
{
  "customerId": 1,
  "isPaid": true
}
```
- List Installments:
```json
{
  "loanId": 1
}
```

- Pay Installment:
```json
{
  "loanId": 1,
  "amount": 500
}
```
