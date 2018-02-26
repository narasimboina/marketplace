
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::

 
SpringBoot and Hibernate/JPA with an embedded Jetty Container.

we have the following objectives:

Database Support via H2
JPA implementation using Hibernate 5
Standardized logging
Maven 3
REST endpoints
Spring Security OAuth
Dependency Injection with Spring
build and deploy the application for local, development, and production environments.
Testing can be accomplished via SpringBootTest using the `test` Spring profile



Non-Functional Requirements
TODO:
1) Code Coverage using SonarQube -Configure using Maven
2) Application Monitoring
3) Swagger Config

Application Build & Deployment

Be sure to have the following installed on your build machine:

Java JDK 1.8
Maven 3

Run : mvn clean install
Java -jar  jarful

1) access below API to get OAuth Token

$ curl -X POST -vu prod-marketplace:987654 http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=password&username=narasim&grant_type=password&scope=write&client_secret=987654&client_id=prod-marketplace"

Respone:
{"access_token":"5b318d13-99fa-4cb7-84b7-34f636b31146","token_type":"bearer","refresh_token":"0ff40f47-e62e-41b5-8db6-72d1434b552f","expires_in":43199,"scope":"write"}

error :  {"error":"invalid_token","error_description":"Invalid access token: 15b318d13-99fa-4cb7-84b7-34f636b31146"}
2) Create project

$ curl -X POST http://127.0.0.1:8080/projects -H "Authorization: Bearer 632abb7d-6f4b-4fb8-a573-96bcc0eb8b8b" -H "Content-Type: application/json" -d '{"projectName" :"Retro3project","description" :"Retro project","budjet" : 9159.0,"closingDate" : "2018-02-28"}'

Response: 200 Ok with location http://127.0.0.1:8080/projects/2

3) create Bid

$ curl -X POST http://127.0.0.1:8080/projects/2/bids -H "Authorization: Bearer 632abb7d-6f4b-4fb8-a573-96bcc0eb8b8b" -H "Content-Type: application/json" -d '{"description" : "bid#2 for 2","quote" : 5960.0}'

4) Get List of Open Projects


curl -X GET http://127.0.0.1:8080/projects -H "Authorization: Bearer 632abb7d-6f4b-4fb8-a573-96bcc0eb8b8b" -H "Content-Type: application/json"
Respone:

[{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":1,"closingDate":"2018-02-28 00:00 AM PST"},{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":2,"closingDate":"2018-02-28 00:00 AM PST"},{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":3,"closingDate":"2018-02-28 00:00 AM PST"},{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":4,"closingDate":"2018-02-28 00:00 AM PST"},{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":5,"closingDate":"2018-02-28 00:00 AM PST"},{"projectName":"Retro3project","description":"Retro project","budjet":9159.0,"id":6,"closingDate":"2018-02-28 00:00 AM PST"}]

5) Get Project with Min Bid

$ curl -X GET http://127.0.0.1:8080/projects/2 -H "Authorization: Bearer 632abb7d-6f4b-4fb8-a573-96bcc0eb8b8b" -H "Content-Type: application/json"

{"id":2,"description":"Retro project","bid":560.0,"projectName":"Retro3project","budjet":9159.0}



The time the exercise took - 10 - 14 hours
Exercise Difficulty:  Moderate
How did you feel about the exercise itself - I feel this exercise will give candidate in depth knowledge and approach for coding
How do you feel about coding an exercise as a step in the interview process  - 10 Awesome
What would you change in the exercise and/or process - provide some non functional requirements for basic like code coverage 
 