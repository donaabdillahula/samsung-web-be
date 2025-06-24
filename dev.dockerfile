# Gunakan base image OpenJDK
FROM openjdk:24-jdk-nanoserver

# Set working directory
WORKDIR /app

# Copy file pom.xml dan download dependencies terlebih dahulu (agar cache efisien)
COPY pom.xml ./
COPY mvnw ./
COPY .mvn ./.mvn

RUN ./mvnw dependency:go-offline

# Copy seluruh source code ke dalam container
COPY . .

# Expose port aplikasi
EXPOSE 8080

# Jalankan aplikasi Spring Boot (ubah nama jar sesuai hasil build)
CMD ["./mvnw", "spring-boot:run"]