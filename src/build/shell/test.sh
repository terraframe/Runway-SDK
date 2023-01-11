#!/bin/bash
#
# Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
#
# This file is part of Runway SDK(tm).
#
# Runway SDK(tm) is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# Runway SDK(tm) is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
#


sudo rm -r /docker-tmp || true
sudo mkdir -p /docker-tmp/test-results && sudo chmod -R 777 /docker-tmp/test-results

sudo docker rm -f $(docker ps -a -q --filter="name=postgres") || true
sudo docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgis/postgis:14-3.2
  
sudo docker rm -f $(docker ps -a -q --filter="name=orientdb") || true
sudo docker run -d --name orientdb -p 2424:2424 -p 2480:2480  -e ORIENTDB_ROOT_PASSWORD=root orientdb:3.0

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
