# samsung-web-be

This is a Spring Boot backend project for Samsung Web.

## Features

-   RESTful API with Spring Boot 3
-   PostgreSQL database integration
-   JPA/Hibernate ORM
-   ImageKit integration for file storage
-   Dockerized for easy deployment
-   Docker Compose support

## Requirements

-   Java 21+ (recommended)
-   Maven 3.9+
-   PostgreSQL database
-   Docker (for containerization)
-   Docker Compose (for multi-service orchestration)

## Docker

## Docker Compose

If you have a `docker-compose.yml` file, you can start all services (including database, etc) with:

```sh
docker-compose up
```

or in detached mode:

```sh
docker-compose up -d
```

### Build Docker Image

```sh
docker build --platform=linux/amd64 -t yourusername/samsung-web-be:latest .
```

### Run Docker Container

```sh
docker run --env-file .env -p 8080:8080 yourusername/samsung-web-be:latest
```

## Build & Run Locally

1. **Clone the repository:**

    ```sh
    git clone https://github.com/donaabdillahula/samsung-web-be.git
    cd samsung-web-be
    ```

2. **Configure environment variables:**

    - Copy `.env.example` to `.env` and fill in your credentials, or set environment variables manually.

3. **Build the JAR:**

    ```sh
    mvn clean package -DskipTests
    ```

4. **Run the application:**
    ```sh
    export $(grep -v '^#' .env | xargs)
    java -jar target/samsung-web-be-0.0.1-SNAPSHOT.jar
    ```

## Environment Variables

The application uses environment variables for configuration. Example:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<db>
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
IMAGEKIT_BOOK_FOLDER=samsung-web
IMAGEKIT_URL_ENDPOINT=https://ik.imagekit.io/your_endpoint
IMAGEKIT_PRIVATE_KEY=your_private_key
IMAGEKIT_PUBLIC_KEY=your_public_key
```

## Deployment

-   Push your Docker image to Docker Hub.
-   Deploy to Render or other cloud platforms using the Docker image.
-   **Note:** Build your image with `--platform=linux/amd64` for compatibility with most cloud providers.

## License

MIT

Made with ❤️ by Dona Abdillah Ula