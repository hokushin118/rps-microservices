## Declare global variables
ARG APP_NAME=WebStatus
ARG TEMP_FOLDER=tmp

FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base

## Refresh global variables
ARG TEMP_FOLDER

# Set working directory
WORKDIR /$TEMP_FOLDER

EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build

## Refresh global variables
ARG APP_NAME
ARG TEMP_FOLDER

# It's important to keep lines from here down to "COPY . ." identical in all Dockerfiles
# to take advantage of Docker's build cache, to speed up local container builds
COPY ./web/$APP_NAME/$APP_NAME.csproj /web/$APP_NAME/$APP_NAME.csproj
COPY ./docker-compose.dcproj /docker-compose.dcproj
COPY ./NuGet.config /NuGet.config
COPY ./web/$APP_NAME /web/$APP_NAME

# Set working directory
WORKDIR /web/$APP_NAME

RUN dotnet publish -c Release -o /$TEMP_FOLDER

FROM build AS publish

FROM base AS final

# Set individual labels
# https://developers.redhat.com/articles/2021/11/11/best-practices-building-images-pass-red-hat-container-certification#best_practice__6__embed_identifying_information_inside_your_image
LABEL summary = $APP_NAME \
      description="The WebStatus application monitors microservices health"

# Set working directory
WORKDIR /$TEMP_FOLDER

COPY --from=publish /$TEMP_FOLDER .

ENV ASPNETCORE_URLS http://*:5000

# Start the microservice
ENTRYPOINT ["dotnet", "WebStatus.dll"]
