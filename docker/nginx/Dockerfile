FROM nginx:1.23.1

## Declare local variables
ARG TEMP_FOLDER=tmp

## Set environment variables
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Set working directory
WORKDIR /$TEMP_FOLDER

RUN rm /etc/nginx/conf.d/default.conf
# Copy Nginx config template
COPY ./infrastructure/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf

# Start Nginxm
ENTRYPOINT ["nginx", "-g", "daemon off;"]
