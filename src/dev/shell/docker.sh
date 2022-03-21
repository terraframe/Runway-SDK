#!/bin/bash
set -e

export POSTGRES_PORT=5442


if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi


cd ../../../
docker build . -t runwaysdk-test


# Not Debug
docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=$POSTGRES_PORT -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError" --network=host runwaysdk-test

# Debug
#docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=$POSTGRES_PORT -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y" --network=host runwaysdk-test






# sudo docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=5442 -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError" --network=host --entrypoint=/bin/cat runwaysdk-test /runwaysdk/envcfg/envcfg.properties