# Content Management API
_This project is designed as a starter project for learning Spring Boot
and gradually improving to a full-featured backend application._

A RESTful API built with Spring Boot for managing articles and categories with JWT-based authentication and role-based authorization.

## Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Security** — JWT authentication & role-based authorization
- **PostgreSQL** — primary database
- **Redis** — JWT token blacklisting for logout
- **Docker** — containerized infrastructure

---

## Prerequisites

- Java 21
- Docker & Docker Compose
- Gradle

---

## Setup & Run

### 1. Clone the repository

### 2. Configure environment variables

Set the following environment variables before running the application using your preferred method (IDE run configuration, system environment, or `application-local.yml`).

### 3. Start infrastructure

```bash
docker-compose up -d
```

This starts PostgreSQL and Redis containers.

### 4. Run the application

The application starts on `http://localhost:8080`.

---

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| `JWT_SECRET` | Secret key for JWT signing (min 32 chars) | `your-secret-key` |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Access token expiry in ms | `300000` (5 min) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Refresh token expiry in ms | `1800000` (30 min) |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `ADMIN_USERNAME` | Seeded admin username | `admin` |
| `ADMIN_EMAIL` | Seeded admin email | `admin@example.com` |
| `ADMIN_PASSWORD` | Seeded admin password | `Admin123@` |

---

## Authentication

The API uses JWT Bearer token authentication.

### Register

```http
POST /api/v1/auth/register
```

### Login

```http
POST /api/v1/auth/login
```

Returns `accessToken` and `refreshToken`.

### Refresh Token

```http
POST /api/v1/auth/refresh
X-Refresh-Token: {refreshToken}
```

### Logout

```http
POST /api/v1/auth/logout
Authorization: Bearer {accessToken}
```

Blacklists the token in Redis — token is immediately invalidated.

---

## Authorization

| Role | Description |
|---|---|
| `USER` | Default role assigned on registration |
| `ADMIN` | Full access including admin endpoints |

---

## API Endpoints

### Auth — `/api/v1/auth` (Public)

| Method | Path | Description |
|---|---|---|
| POST | `/register` | Register a new user |
| POST | `/login` | Login and get tokens |
| POST | `/refresh` | Refresh access token |
| POST | `/logout` | Logout and invalidate token |

### Users — `/api/v1/users` (Authenticated)

| Method | Path | Description |
|---|---|---|
| GET | `/me` | Get current user profile |
| PUT | `/me` | Update current user profile |
| PATCH | `/me/password` | Change password |
| DELETE | `/me` | Soft delete account |

### Admin Users — `/api/v1/admin/users` (ADMIN only)

| Method | Path | Description |
|---|---|---|
| POST | `/search` | Search and filter users |
| GET | `/{id}` | Get user by ID |
| PATCH | `/{id}/role` | Update user role |
| PATCH | `/{id}/status` | Enable/disable user |

### Categories — `/api/v1/categories` (Mixed)

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/` | Authenticated | Get all categories |
| GET | `/{id}` | Authenticated | Get category by ID |
| POST | `/` | ADMIN | Create category |
| PUT | `/{id}` | ADMIN | Update category |
| PATCH | `/{id}/status` | ADMIN | Change category status |

### Articles — `/api/v1/articles` (Mixed)

| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/search` | Authenticated | Search and filter articles |
| GET | `/{id}` | Authenticated | Get article by ID |
| POST | `/` | Authenticated | Create article |
| PUT | `/{id}` | Owner | Update own article |
| DELETE | `/{id}` | Owner or ADMIN | Delete article |
| PATCH | `/{id}/publish` | ADMIN | Publish article |

---

## Error Response Format

```json
{
  "errorCode": "resource_not_found",
  "errorType": "NOT_FOUND",
  "errorMessage": "Article not found with id - 1",
  "path": "/api/v1/articles/1",
  "timestamp": "2026-04-29T10:00:00"
}
```

---

## Running Tests

Unit tests are provided for all service and controller layers.
