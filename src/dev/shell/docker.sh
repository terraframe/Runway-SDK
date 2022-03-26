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


# If you want to only run one test suite
# docker run --name runwaysdk-test --rm --network=host \
# -e DATA_ACCESS_TEST_SUITE=false -e BUSINESS_TEST_SUITE=true -e FACADE_TEST_SUITE=false \
# -e QUERY_TEST_SUITE=false -e MULTITHREADED_TEST_SUITE=false -e ONTOLOGY_TEST_SUITE=false \
# -e SESSION_TEST_SUITE=false -e GRAPH_TEST_SUITE=false -e VAULT_TEST_SUITE=false \
# -e POSTGRES_PORT=5442 -e MAVEN_OPTS="-Xmx3500M -Xms256M -XX:+HeapDumpOnOutOfMemoryError -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" \
# -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports \
# runwaysdk-test
