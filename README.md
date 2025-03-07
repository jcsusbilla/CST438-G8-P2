# Tier List API

## Overview
This API provides endpoints for managing tier lists and user authentication.

## Base URLs
- Tier List: `https://localhost:8080/tierlists`
- User: `https://localhost:8080/user`
- User-Tier Lists: `https://localhost:8080/userTierLists`

## Endpoints

### Tier List Endpoints
#### Create a Tier List
- **POST** `/tierlists/add`
- **Parameters:**
  - `title` (String) - Required
  - `subject` (String) - Required
  - `weekStartDate` (String) - Optional, defaults to current date
- **Response:** `"Tier List saved"`

#### Delete a Tier List
- **DELETE** `/tierlists/delete?id={id}`
- **DELETE** `/tierlists/delete/{id}`
- **Response:** `"Tier List deleted successfully"` or `"Tier List does not exist"`

#### Update a Tier List
- **PUT** `/tierlists/update/{id}`
- **Parameters:**
  - `title` (String) - Optional
  - `subject` (String) - Optional
  - `date` (String) - Optional
- **Response:** `"Tier List updated successfully."` or `"Update Failed: No parameters selected for change!"`

#### Retrieve Tier Lists
- **GET** `/tierlists/all` - Returns all tier lists
- **GET** `/tierlists/{id}` - Returns tier list by ID
- **GET** `/tierlists/title/{title}` - Returns tier list by title
- **GET** `/tierlists/subject/{subject}` - Returns tier list by subject

### User Endpoints
#### Register a User
- **POST** `/user/add`
- **Parameters:**
  - `user_name` (String) - Required
  - `email` (String) - Required
  - `password` (String) - Required
  - `first_name` (String) - Required
  - `last_name` (String) - Required
- **Response:** `"User registered successfully!"` or error message if username or email already exists

#### User Login
- **POST** `/user/login`
- **Parameters:**
  - `email` (String) - Required
  - `password` (String) - Required
- **Response:** `"Login successful! Welcome {user_name}"` or `"Incorrect password. Please try again!"`

#### User Logout
- **GET** `/user/logout`
- **Response:** `"Logged out successfully!"`

#### Retrieve Users
- **GET** `/user/all` - Returns all users
- **GET** `/user/{id}` - Returns user by ID

### User-Tier List Endpoints
#### Associate User with Tier List
- **POST** `/userTierLists/addTierList`
- **Parameters:**
  - `userId` (Integer) - Required
  - `tierId` (Integer) - Required
- **Response:** `"UserTierList created successfully."` or error message if user or tier list is not found.

#### Remove User-Tier List Association
- **DELETE** `/userTierLists/deleteTierList/{id}`
- **Response:** `"UserTierList deleted successfully."` or `"Tier List not found with ID: {id}"`

## Dependencies
- Spring Boot
- Spring Data JPA
- Spring Security (BCrypt for password hashing)
- JDBC Template (for raw SQL queries)

## Notes
- Tier List entity only contains metadata; rankings are managed separately.
- Passwords are securely hashed before storage.
- Sessions are used for user authentication.

## Future Enhancements
- Add retrieval of tier lists by date
  

