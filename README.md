# Employee API

The Employee API is a Spring Boot application that allows you to manage employee data. It provides endpoints for creating, updating, retrieving, and deleting employees, as well as exposing API documentation using Swagger. This project integrated with Kafka for producing any update on Employee.

# Requirements
- [Docker](https://www.docker.com/get-started)
- Docker Compose
- [Apache maven](https://maven.apache.org/install.html)

# Project Structure

The project follows the Domain-Driven Design (DDD) principles and is structured as mentioned in below:
- main
  - java
    - com.meysamzamani.employee
      - application
      - domain 
      - infrastructure
        - kafka
        - persistence
      - presentation
        - controller
        - dto
        - exceptions
        - validation
    - resources 
- test

# How to Build and Run

1. Clone the repository to your local machine:

        git clone https://github.com/meysamzamani/EmployeeAPI.git
        cd EmployeeAPI

2. Build the project using Maven:

        mvn clean package -Pprod

    The build artifacts will be generated in the target directory.


3. Start the Docker Compose environment:
        
        docker-compose up -d
    
    If you're using MacOSX you can run this command

        docker compose up -d

    The Docker Compose command will start all the services defined in the docker-compose.yml file, including Kafka, ZooKeeper, PostgreSQL, and the Spring Boot application.


4. Access the application:

    Once the Docker Compose environment is up and running, you can access the RESTful APIs of the Spring Boot application at [This URL](http://localhost:8080).

# APIs Documentation (Swagger)

The application exposes its RESTful APIs using Swagger. Once the application is running, you can access the Swagger UI to explore and interact with the APIs at the following URL:

[Swagger Url](http://localhost:8080/swagger-ui.html)

# Kafka Integration

The application uses Kafka to publish events related to employee creation, update, and deletion. Kafka configuration and integration are handled in the KafkaConfiguration class and the Kafka-related components in the infrastructure.kafka package.

Kafka topics are created automatically when events are published for the first time.

