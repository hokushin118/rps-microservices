FROM nginx:1.23.3-alpine

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
# Copy Nginx certificates
#COPY ./infrastructure/nginx/certs/* /etc/nginx/ssl/rps/

EXPOSE 80
#EXPOSE 443

# Start Nginx
ENTRYPOINT ["nginx", "-g", "daemon off;"]
