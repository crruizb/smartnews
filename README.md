# Smart news

This is an application composed of frontend+backend that fetches news from different sources of Spanish newspapers so that the user can read the latest news.

It also allows to login with Google so that the user can then rate every news and then the system can recommend other news based on the user ratings and other user ratings which are similar.

This was a project done for the TFG of the UPC FIB and the thesis can be found here: https://upcommons.upc.edu/handle/2117/352140 (Spanish).

Live at: https://smartnews.pages.dev

## Backend

### How to run

First start the postgres database with the docker-compose.yml inside ops:

```
cd ops
docker-compose up -d
```

Then run the application with Maven:

```
mvn spring-boot:run
```

## Frontend

### How to run

```
pnpm install
pnpm run dev
```

## Stack

- Java 21
- Spring Boot 3.4.4
- React
- Vite
