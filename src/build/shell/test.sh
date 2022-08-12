#!/bin/bash

sudo rm -r /docker-tmp || true
sudo mkdir -p /docker-tmp/test-results && sudo chmod -R 777 /docker-tmp/test-results

sudo docker rm -f $(docker ps -a -q --filter="name=postgres") || true
sudo docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 mdillon/postgis:9.5
  
sudo docker rm -f $(docker ps -a -q --filter="name=orientdb") || true
sudo docker run -d --name orientdb -p 2424:2424 -p 2480:2480  -e ORIENTDB_ROOT_PASSWORD=root orientdb:3.0.25

sudo docker build -t runwaysdk-test .

set +e
sudo -E docker run --name runwaysdk-test --rm --network=host \
-v /docker-tmp/test-results:/runwaysdk/runwaysdk-test/target/surefire-reports \
-e DATA_ACCESS_TEST_SUITE=true -e BUSINESS_TEST_SUITE=true -e FACADE_TEST_SUITE=true \
-e VAULT_TEST_SUITE=true -e QUERY_TEST_SUITE=true -e MULTITHREADED_TEST_SUITE=false \
-e ONTOLOGY_TEST_SUITE=true -e SESSION_TEST_SUITE=true -e GRAPH_TEST_SUITE=true \
-e MAVEN_OPTS="-Xmx3500M -Xms256M -XX:+HeapDumpOnOutOfMemoryError" \
runwaysdk-test
ecode=$?
mkdir -p "$WORKSPACE/test-results"
sudo cp -r /docker-tmp/test-results/. "$WORKSPACE/test-results/"
sudo chmod 777 -R $WORKSPACE/test-results
sudo chown ec2-user:ec2-user -R "$WORKSPACE/test-results"

sudo ls -al /docker-tmp/test-results
ls -al "$WORKSPACE/test-results"

set -e
[ "$ecode" != 0 ] && exit $ecode;
exit 0;
