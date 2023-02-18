#!/bin/bash

# Determine sed command options based on the OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    SED_OPTS="-i .back"
else
    SED_OPTS="-i"
fi

# Generate unique words
USERNAME_MONGO_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
PASSWORD_MONGO_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
REDIS_PASSWORD_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
JWT_SECRET_KEY_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
REFRESH_JWT_SECRET_KEY_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
JWT_SERVICE_USERNAME_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')
JWT_SERVICE_PASSWORD_GENERATED=$(shuf -n 1 /usr/share/dict/words | tr '[:upper:]' '[:lower:]')

# Replace the passwords in the Docker Compose file
sed $SED_OPTS "s/USERNAME_MONGO/$USERNAME_MONGO_GENERATED/g" docker-compose-dependency.yml
sed $SED_OPTS "s/PASSWORD_MONGO/$PASSWORD_MONGO_GENERATED/g" docker-compose-dependency.yml
sed $SED_OPTS "s/REDIS_PASSWORD/$REDIS_PASSWORD_GENERATED/g" docker-compose-dependency.yml

# Replace the passwords in the application-dev.yml file
sed $SED_OPTS "s/JWT_SECRET_KEY/$JWT_SECRET_KEY_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/REFRESH_JWT_SECRET_KEY/$REFRESH_JWT_SECRET_KEY_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/JWT_SERVICE_USERNAME/$JWT_SERVICE_USERNAME_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/JWT_SERVICE_PASSWORD/$JWT_SERVICE_PASSWORD_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/USERNAME_MONGO/$USERNAME_MONGO_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/PASSWORD_MONGO/$PASSWORD_MONGO_GENERATED/g" src/main/resources/application-dev.yml
sed $SED_OPTS "s/REDIS_PASSWORD/$REDIS_PASSWORD_GENERATED/g" src/main/resources/application-dev.yml