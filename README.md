# Authentication Service

This is a basic authentication service built using the Spring Boot framework and the Model-View-Controller (MVC) architecture. The service allows users to register, login, and manage their account information.  

## Medium Post
I've also written a Medium post about this project, 
which you can read at https://medium.com/@sadcat/build-your-own-authentication-service-with-spring-boot-cacd22d72851.
The post goes into more detail about the development process and how to use the app.

## Prerequisites
To build and run this application, you'll need the following tools:
- Docker
- Maven 3^
- Java 19^

## Setup
Clone this repository to your local machine.

#### Generate credentials:
Open a terminal and navigate to the root directory of the project, then run the following command:
```bash
bash generate_passwords.sh
```

- This will generate unique passwords for the MongoDB and Redis databases used by the application, as well as for the JSON Web Tokens (JWT) used for authentication.

### ⚠️ Replace the KAVENEGAR_YOUR_API_KEY placeholder in the src/main/resources/application-dev.yml file with your Kavenegar API key.

Build and run the Docker dependencies by running the following command in your terminal:
```bash
docker-compose -f docker-compose-dependency.yml up -d --build
```
> This will start MongoDB and Redis containers in the background.

Build and run the application by running the following command in your terminal:
```bash
mvn spring-boot:run
```
> This will start the application on http://localhost:8080.

Build command:
```bash
mvn clean package
```

#### ⚠️ Note on Maven version :
This application was built and tested with Apache Maven 3.9.0. If you encounter any issues with other versions of Maven, please try using version 3

## Usage
The authentication service provides the following endpoints:

### User registration
To register a new user, send a `POST` request to the `/api/v1/users/signup` endpoint with a JSON payload containing the following fields:
```json
{     
	"fullName": "Your Name",     
	"username": "your_username",     
	"password": "your_password",     
	"phoneNumber": "your_phone_number" 
}
```
If the registration is successful, the server will respond with a `201 Created` status code.

### User login
To log in a user, send a `POST` request to the `/api/v1/users/signin` endpoint with a JSON payload containing the following fields:
```json 
{
	"username": "your_username",     
	"password": "your_password" 
}
```
If the login is successful, the server will respond with a `200 OK` status code and a JSON payload containing a JWT token and Refresh token.

### User profile
To retrieve a user's profile information, send a `GET` request to the `/api/v1/users/me/profile` endpoint.
If the token is valid and the user exists, the server will respond with a `200 OK` status code and a JSON payload containing the user's full name, birthday, bio.
