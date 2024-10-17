# Use an image with Maven and JDK 17
FROM maven:3.8.3-openjdk-17

# Set working directory inside the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .
COPY conveyor/pom.xml ./conveyor/pom.xml

# Download dependencies based on pom.xml
RUN mvn dependency:go-offline -B

# Copy the rest of the application source code
COPY conveyor/src ./src

# Build the application
RUN mvn package

# Copy the built JAR file from the previous stage
COPY conveyor/target/conveyor-1.0-SNAPSHOT.jar app.jar

# Define the entry point for running the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
