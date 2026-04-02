# Finance Backend – Data Processing and Access Control

This project is a backend system built using Spring Boot to manage financial records with role-based access control. The goal of this implementation is to simulate a real-world backend that handles user permissions, financial data processing, and summary analytics for a dashboard.

The system is designed with a layered architecture and focuses on clean separation of concerns, business logic clarity, and practical API design.

---

## Overview

The backend supports:

- User management with roles and status
- Financial record management (income and expenses)
- Role-based access control for different operations
- Dashboard-level aggregated insights
- Filtering and querying financial data

The application uses MySQL for persistence and Spring Data JPA for database interaction.

---

## Architecture and Design

The project follows a standard layered architecture:

### Controller Layer
Handles HTTP requests and defines API endpoints. It also performs basic validation and enforces role-based restrictions before delegating logic to the service layer.

### Service Layer
Contains the core business logic. All data processing such as filtering, aggregation, and validation rules are implemented here.

### Repository Layer
Uses Spring Data JPA to interact with the database. It abstracts database queries and provides easy-to-use methods for CRUD operations and filtering.

### Model Layer
Defines the entities (`User` and `Record`) and their relationships. Each financial record is associated with a user.

---

## Data Model

### User
Represents a system user.

Fields:
- id
- name
- email (unique)
- role (ADMIN, ANALYST, VIEWER)
- status (ACTIVE, INACTIVE)

### Record
Represents a financial transaction.

Fields:
- id
- amount
- type (INCOME or EXPENSE)
- category
- date
- notes
- user (Many-to-One relationship with User)

---

## Role-Based Access Control

The system enforces access rules based on user roles:

- ADMIN  
  Full access. Can create, update, and delete records and manage users.

- ANALYST  
  Can view records and access dashboard insights but cannot modify data.

- VIEWER  
  Limited to viewing dashboard summaries only.

These checks are implemented at the controller level to clearly demonstrate backend enforcement of permissions.

---

## Features Implemented

### 1. User Management
- Create users
- Assign roles
- Maintain user status (ACTIVE / INACTIVE)
- Enforce unique email constraint

### 2. Financial Records CRUD
- Create records (restricted to ADMIN)
- View records
- Update records (ADMIN only)
- Delete records (ADMIN only)

### 3. Record Filtering

Records can be filtered based on:

- User ID
- Type (INCOME / EXPENSE)
- Category
- Date

This is implemented using repository-level query methods for efficient filtering.

---

### 4. Dashboard Summary APIs

The backend provides aggregated data suitable for dashboards:

- Total income
- Total expenses
- Net balance
- Category-wise totals
- Monthly trends
- Recent activity

These computations are handled in the service layer to separate business logic from API definitions.

---

### 5. Validation and Error Handling

The application includes basic validation such as:

- Amount must be greater than zero
- Required fields validation
- Role-based access restrictions
- Unique email constraint at database level

Errors are returned with meaningful messages to indicate invalid operations.

---

## API Endpoints

### User APIs

- POST `/users`  
  Create a new user

- GET `/users`  
  Retrieve all users

---

### Record APIs

- POST `/records`  
  Create a new record (ADMIN only)

- GET `/records`  
  Retrieve all records

- GET `/records/user/{userId}`  
  Retrieve records for a specific user

- PUT `/records/{id}`  
  Update a record (ADMIN only)

- DELETE `/records/{id}?userId={userId}`  
  Delete a record (ADMIN only)

---

### Filtering APIs

- GET `/records/filter/type?userId={id}&type=INCOME`

- GET `/records/filter/category?userId={id}&category=Salary`

- GET `/records/filter/date?userId={id}&date=YYYY-MM-DD`

---

### Dashboard APIs

- GET `/records/summary/{userId}/income`

- GET `/records/summary/{userId}/expense`

- GET `/records/summary/{userId}/balance`

---

## Sample Requests

### Create User

```json
{
  "name": "Shruthi",
  "email": "shruthi@gmail.com",
  "role": "ADMIN",
  "status": "ACTIVE"
}
```
## Database Configuration

- The application uses MySQL.

- Example configuration:

- spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
- spring.datasource.username=your_username
- spring.datasource.password=your_password
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.show-sql=true

## How to Run
- Clone the repository
- Create a MySQL database named finance_db
- Configure database credentials in application.properties
- Run the Spring Boot application
- Access APIs at http://localhost:8080

## Assumptions

- Role is passed as part of the request payload  
- Users exist before records are created  
- Role validation is handled at the controller level for simplicity  

---

## Possible Improvements

- Implement authentication using JWT-based login  
- Add global exception handling for consistent error responses  
- Introduce pagination for handling large datasets  
- Support combined filtering with multiple query parameters  
- Deploy the application to a cloud platform for public access  

---

## Conclusion

This project focuses on demonstrating backend fundamentals such as structured architecture, role-based access control, data modeling, and aggregation logic.  

The implementation goes beyond basic CRUD operations by incorporating access control and dashboard-level insights, reflecting how a real-world backend system would be designed and used.



