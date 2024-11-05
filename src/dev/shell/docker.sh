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

set -e

export POSTGRES_PORT=5442


if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi


cd ../../../
docker build . -t runwaysdk-test


# Not Debug
#docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=$POSTGRES_PORT -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError" --network=host runwaysdk-test

# Debug
docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=$POSTGRES_PORT -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y" --network=host runwaysdk-test


# If you want to only run one test suite
# docker run --name runwaysdk-test --rm --network=host \
# -e DATA_ACCESS_TEST_SUITE=false -e BUSINESS_TEST_SUITE=true -e FACADE_TEST_SUITE=false \
# -e QUERY_TEST_SUITE=false -e MULTITHREADED_TEST_SUITE=false -e ONTOLOGY_TEST_SUITE=false \
# -e SESSION_TEST_SUITE=false -e GRAPH_TEST_SUITE=false -e VAULT_TEST_SUITE=false \
# -e POSTGRES_PORT=5442 -e MAVEN_OPTS="-Xmx3500M -Xms256M -XX:+HeapDumpOnOutOfMemoryError -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" \
# -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports \
# runwaysdk-test
