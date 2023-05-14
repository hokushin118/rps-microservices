## Declare global variables
ARG APP_NAME=rps-qry-service
ARG TEMP_FOLDER=tmp

# Step 1: BUILD STAGE
FROM maven:3.8.5-openjdk-11 as build

## Refresh global variables
ARG APP_NAME
ARG TEMP_FOLDER

## Set environment variables
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Set /tmp as the working directory
WORKDIR /$TEMP_FOLDER

# Copy .m2 configuration settings to the container folder
COPY ./infrastructure/settings /settings

# Copy the proto messages to the container folder
COPY ./proto /proto

# Copy the rps-grpc-lib project and build it
COPY ./common/rps-grpc-lib /common/rps-grpc-lib
RUN mvn clean install -f /common/rps-grpc-lib/pom.xml -s /settings/settings.xml -DskipTests

# Copy the cqrs-es-framework project and build it
COPY ./common/cqrs-es-framework /common/cqrs-es-framework
RUN mvn clean install -f /common/cqrs-es-framework/pom.xml  -s /settings/settings.xml -DskipTests

# Copy the rps-common-lib project and build it
COPY ./common/rps-common-lib /common/rps-common-lib
RUN mvn clean install -f /common/rps-common-lib/pom.xml  -s /settings/settings.xml -DskipTests

# Copy the rps-qry-service project and build it
COPY ./microservices/$APP_NAME /microservices/$APP_NAME
RUN mvn clean package -f /microservices/$APP_NAME/pom.xml  -s /settings/settings.xml -DskipTests

# Step 2: APPLICATION CONTAINER
FROM openjdk:11.0.11-jre-slim AS publish

## Refresh global variables
ARG APP_NAME
ARG TEMP_FOLDER

# Set individual labels
# https://developers.redhat.com/articles/2021/11/11/best-practices-building-images-pass-red-hat-container-certification#best_practice__6__embed_identifying_information_inside_your_image
LABEL summary = $APP_NAME \
      description="The Rock Paper Scissors game query microservice"
LABEL "collect_logs_with_filebeat"="true"
LABEL "decode_log_event_to_json_object"="true"

# Set /tmp as the working directory
WORKDIR /$TEMP_FOLDER

# Copy the jar file into the Docker image
COPY --from=build /microservices/$APP_NAME/target/$APP_NAME.jar app.jar

# Expose ports (http grpc)
EXPOSE 8080 6565

# Start the microservice
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","app.jar"]
