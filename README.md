# Loan Management System

## Overview
Backend Loan API for bank employees to manage customer loans with secure authentication and advanced payment features.

## Technology Stack
- Java 23
- Spring Boot 3.x
- Spring Security
- H2 Database
- JUnit 5
- Mockito

## Features
- Create loans with flexible configurations
- List customer loans and installments
- Pay loan installments with smart payment logic
- Role-based authentication (Admin/Customer)
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
