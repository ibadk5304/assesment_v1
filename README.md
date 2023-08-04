# assesment_v1
# Running the Application 
To run the application use the following command:
mvn spring-boot: run

The application will start on port 8080 by default

# API Endpoints
The application exposes the following endpoint:
* POST / register
Use postman to send the post request where need to add/modify:

Header:{
 Content-Type: application/json
}

Body: {
  "username": "test_user",
  "password": "Password#1",
  "ipAddress": "24.48.0.1"
}
