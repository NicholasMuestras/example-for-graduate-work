# README

## Description
This is a backend for an advertisement service.
The system provides the following functionality:


__1. User Management:__

- User registration and authentication
- Password management
- Profile information management (first name, last name, phone)
- User avatar upload
- Role-based access control (USER, ADMIN)

__2. Advertisement Management:__

- Create, read, update, and delete advertisements
- Upload and update advertisement images
- View all advertisements
- View user's own advertisements

__3. Comment System:__

- Add comments to advertisements
- View comments for specific advertisements
- Edit and delete comments
- Key entities in the system:

__Users:__ Registered individuals who can post advertisements and comments

__Advertisements:__ Items or services being offered, with title, price, description, and image

__Comments:__ User feedback on specific advertisements

__Images:__ Associated with both user profiles and advertisements

The API supports standard CRUD operations for all main entities with proper authentication and authorization controls.

## Stack

- Java 11
- Spring Boot 2.7.15
- Spring Web (Spring MVC)
- Spring Data JPA
- Spring Security
- PostgreSQL (production database)
- H2 (test database)
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Maven (build tool)
- JUnit (testing)
- Jackson (JSON processing)
- Bean Validation API (JSR-303)

## Quick start

1. **Prerequisites**
   - Java 11
   - Maven 3.6+
   - PostgreSQL (optional, for production)

2. **Build the application**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/ads-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application**
   - API documentation: http://localhost:8080/swagger-ui.html
   - Base API URL: http://localhost:8080

5. **Environment configuration**
   The application uses default settings for H2 database in development. 
   To configure PostgreSQL, modify the application.properties file with your database settings.

6. **Testing**
   Run tests with:
   ```bash
   ./mvnw test
   ```

## Known issues

### Frontend
for docker image `ghcr.io/dmitry-bizin/front-react-avito:1.22`

#### 1. Image displaying (Card details page for Admin or Author): 
Request GET localhost:8080/ads/3
Response: 200
{
"pk": 3,
"authorFirstName": "Ben",
"authorLastName": "Gun",
"description": "description",
"email": "user2@gmail.com",
"image": "/images/1769962511229_staff-03.jpeg",
"phone": "+71234567890",
"price": 500,
"title": "Best Staff"
}

Frontend transform "image": 
from "/images/1769962511229_staff-03.jpeg"
to "&quot;http://localhost:8080/images/1769962511229_staff-03.jpeg&quot;"

And the image doesn't show in product's Card. 

#### 2. Password updating: 
Method must be POST according the openapi.yml, but it's PATCH.
