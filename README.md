# TaskFlow - Task Management Application

A modern, full-stack task management application built with Spring Boot and Angular.

## Overview

TaskFlow is a comprehensive task management system that helps you organize and track your tasks efficiently. The application features user authentication, task creation, editing, and status management with a clean, responsive user interface.

## Features

- **User Authentication**: Secure signup and login functionality
- **Task Management**: Create, update, and delete tasks
- **Task Categorization**: View tasks by status (Completed, In Progress, Overdue)
- **Due Date Tracking**: Set due dates and times for tasks
- **Notifications**: Get notified before task deadlines

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.1
- Spring Security with JWT Authentication
- Spring Data JPA
- PostgreSQL Database
- Lombok
- Maven

### Frontend
- Angular 19
- Angular Material
- RxJS
- NgxMaterialTimepicker
- SCSS for styling

### DevOps
- Docker & Docker Compose for containerization
- Nginx for frontend serving and API proxying

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Git

### Installation and Setup

1. Clone the repository:
```bash
git clone https://github.com/Samuel-k276/TaskFlow.git
cd TaskFlow
```

2. Start the application with Docker Compose:
```bash
docker-compose up -d
```

3. Access the application:
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080/api

## Development Setup

### Backend
1. Navigate to the backend directory:
```bash
cd task-app-backend
```

2. Create a `.env` file with the following environment variables:
```
DB_URL=jdbc:postgresql://localhost:5432/taskdb
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000
SERVER_PORT=8080
FRONTEND_URL=http://localhost:4200
```

3. Build and run with Maven:
```bash
mvn spring-boot:run
```

### Frontend
1. Navigate to the frontend directory:
```bash
cd task-app-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Run the development server:
```bash
npm run start
```

## API Endpoints

### Authentication
- `POST /api/auth/register`: Register a new user
- `POST /api/auth/authenticate`: Authenticate a user

### Tasks
- `GET /api/tasks`: Get all tasks for the authenticated user
- `GET /api/tasks/{id}`: Get a specific task
- `POST /api/tasks`: Create a new task
- `PUT /api/tasks/{id}`: Update an existing task
- `DELETE /api/tasks/{id}`: Delete a task

## Project Structure

### Backend
```
task-app-backend/
├── src/main/java/taskapp/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── exception/
│   ├── repository/
│   ├── security/
│   └── service/
└── src/main/resources/
    └── application.properties
```

### Frontend
```
task-app-frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   ├── config/
│   │   ├── guards/
│   │   ├── interceptors/
│   │   ├── models/
│   │   └── services/
│   ├── assets/
│   └── styles.css
└── angular.json
```


