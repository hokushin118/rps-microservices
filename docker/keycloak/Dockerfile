FROM bitnami/keycloak:18.0.0-debian-11-r7

## Declare local variables
ARG TEMP_FOLDER=tmp

## Set environment variables
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Set working directory
WORKDIR /$TEMP_FOLDER

RUN mkdir -p /opt/bitnami/keycloak/data/h2

# Copy realm data
COPY ./infrastructure/keycloak/rps-dev-realm.json /opt/bitnami/keycloak/data/import/rps-dev-realm.json

# Start Keycloak
ENTRYPOINT ["/opt/bitnami/keycloak/bin/kc.sh", "start-dev --import-realm"]
