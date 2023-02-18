Authentication Service
This is an example authentication service built using the Model-View-Controller (MVC) architecture. The service allows users to register, login, and manage their account information.

Prerequisites
To build and run this application, you'll need the following tools:

Docker
Maven
Java 19.0.2
Setup
Clone this repository to your local machine.

Generate credentials by running the generate_passwords.sh script. Open a terminal and navigate to the root directory of the project, then run the following command:

bash
Copy code
bash generate_passwords.sh
This will generate unique passwords for the MongoDB and Redis databases used by the application, as well as for the JSON Web Tokens (JWT) used for authentication.

Replace the KAVENEGAR_YOUR_API_KEY placeholder in the src/main/resources/application-dev.yml file with your Kavenegar API key.

Build and run the Docker dependencies by running the following command in your terminal:

bash
Copy code
docker-compose -f docker-compose-dependency.yml up -d --build
This will start MongoDB and Redis containers in the background.

Build and run the application by running the following command in your terminal:

bash
Copy code
mvn spring-boot:run
This will start the application on http://localhost:8080.

Usage
The authentication service provides the following endpoints:

User registration
To register a new user, send a POST request to the /api/v1/auth/signup endpoint with a JSON payload containing the following fields:

json
Copy code
{
    "username": "your_username",
    "email": "your_email@example.com",
    "password": "your_password"
}
If the registration is successful, the server will respond with a 201 Created status code and a JSON payload containing the user's ID and email address.

User login
To log in a user, send a POST request to the /api/v1/auth/signin endpoint with a JSON payload containing the following fields:

json
Copy code
{
    "usernameOrEmail": "your_username_or_email",
    "password": "your_password"
}
If the login is successful, the server will respond with a 200 OK status code and a JSON payload containing the user's ID, email address, and a JWT token.

User profile
To retrieve a user's profile information, send a GET request to the /api/v1/users/{userId} endpoint, where userId is the ID of the user you want to retrieve.

If the user exists, the server will respond with a 200 OK status code and a JSON payload containing the user's ID, username, and email address.

Update user profile
To update a user's profile information, send a PUT request to the /api/v1/users/{userId} endpoint with a JSON payload containing the fields you want to update. The payload should contain one or more of the following fields:

json
Copy code
{
    "username": "new_username",
    "email": "new_email@example.com",
    "password": "new_password"
}
If the update is successful, the server will respond with a 200 OK status code and a JSON payload containing the updated user's ID, username, and email address.

Note on Maven version
This application was built and tested with Apache Maven 3.9.0. If you encounter any issues with other versions of Maven, please try using version 3# auth-service-springboot
