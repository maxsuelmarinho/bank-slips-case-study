#!/bin/bash

APPLICATIONS="api-gateway bank-slips-api service-discovery";

for application in $APPLICATIONS; do
  echo "===================================="
  echo "Building docker image of the $application application..."
  echo "===================================="

  bash gradlew -x test $application:clean $application:build $application:dockerImage
done