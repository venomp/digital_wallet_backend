# digital_wallet_backend




---


```
wallet-api
```


```
A secure REST API for a digital wallet system built with Spring Boot — supports user auth, JWT security, wallet management, deposits, and peer-to-peer transfers.
```



```
# 💰 Wallet API

A production-ready RESTful wallet backend built with Spring Boot 3, Spring Security, and MySQL.

## Features
- User registration & login with BCrypt password hashing
- JWT-based stateless authentication
- Wallet creation (auto-created on registration)
- Deposit funds into your wallet
- Peer-to-peer transfers with deadlock-safe pessimistic locking
- Paginated transaction history
- Global exception handling with structured JSON error responses
- Input validation on all endpoints

## Tech Stack
- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA + Hibernate
- MySQL
- JJWT (JSON Web Tokens)
- Maven

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /auth/register | ❌ | Register a new user |
| POST | /auth/login | ❌ | Login and receive JWT |
| GET | /wallet/balance | ✅ | Get current balance |
| POST | /wallet/deposit | ✅ | Deposit funds |
| POST | /wallet/transfer | ✅ | Transfer to another user |
| GET | /transactions | ✅ | Paginated transaction history |

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Setup
1. Clone the repo
   git clone https://github.com/your-username/wallet-api.git

2. Create the database
   CREATE DATABASE wallet;

3. Set environment variables
   DB_PASSWORD=your_db_password
   JWT_SECRET=your_secret_key_min_32_chars

4. Run the app
   ./mvnw spring-boot:run

## Environment Variables

| Variable | Description |
|----------|-------------|
| DB_PASSWORD | MySQL password |
| JWT_SECRET | Secret key for signing JWT tokens (min 32 chars) |
```

---


```
spring-boot  java  rest-api  jwt  mysql  spring-security  wallet  backend
```

