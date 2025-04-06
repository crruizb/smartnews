# Smart news backend

This is the backend for the Smart news which is responsible of downloading news from different sources as provide an API for the frontend to use.

## How to run

First start the postgres database with the docker-compose.yml inside ops:

```
cd ops
docker-compose up -d
```

Then run the application with Maven:

```
mvn spring-boot:run
```

## Stack

- Java 21
- Spring Boot 3.4.4
