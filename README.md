# Serverless Contact API - Hexagonal Architecture ⚙️

## 📌 Overview
This repository contains the backend infrastructure for my personal portfolio. It is a 100% Serverless API built with Java, designed to handle contact form submissions securely, persistently, and at scale.

## 🏗️ Architecture Design (Hexagonal)
The core business logic is completely isolated from the delivery mechanisms and infrastructure tools.
* **Domain Layer:** Core entities and validation rules.
* **Application Layer:** Use case orchestrators (Ports).
* **Infrastructure Layer:** Adapters for AWS services (Lambda Handlers, DynamoDB Repositories, SES Email Service).

## ☁️ AWS Services Used (AWS SDK v2)
* **API Gateway:** Entry point handling strict CORS policies.
* **AWS Lambda (Java 21):** Execution environment for the business logic.
* **Amazon DynamoDB:** NoSQL persistence for audit and record-keeping of messages.
* **Amazon SES:** Reliable email delivery system.

## 🛡️ Security
* Strict CORS configuration tied to the production frontend domain.
* Principle of Least Privilege applied to Lambda IAM Execution Roles (only allowed to `PutItem` in Dynamo and `SendEmail` in SES).
