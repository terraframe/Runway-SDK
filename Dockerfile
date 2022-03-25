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

FROM maven:3-openjdk-8

ENV DATA_ACCESS_TEST_SUITE=true
ENV BUSINESS_TEST_SUITE=true
ENV FACADE_TEST_SUITE=true
ENV VAULT_TEST_SUITE=true
ENV QUERY_TEST_SUITE=true
ENV MULTITHREADED_TEST_SUITE=true
ENV ONTOLOGY_TEST_SUITE=true
ENV SESSION_TEST_SUITE=true
ENV GRAPH_TEST_SUITE=true

ENV LOG_LEVEL=warning

ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5432
ENV ORIENTDB_HOST="remote:localhost"

ENV RUNWAY_WORKSPACE=/runwaysdk

ENV MAVEN_OPTS="-Xmx3500M -Xms256M -XX:+HeapDumpOnOutOfMemoryError"
ENV MAVEN_TEST_FORK_COUNT=0

ENV RUNWAY_TEST_IGNORE_DIMENSION_TESTS=true

RUN mkdir $RUNWAY_WORKSPACE
WORKDIR $RUNWAY_WORKSPACE

# Copy the source in
COPY . $RUNWAY_WORKSPACE

# Log4j properties
RUN wget -nv -O $RUNWAY_WORKSPACE/runwaysdk-test/src/main/resources/log4j2.xml https://raw.githubusercontent.com/terraframe/geoprism-cloud/dev/ansible/roles/webserver/files/log4j2.xml
RUN sed -i -e "s/<Root level=\"error\">/<Root level=\"$LOG_LEVEL\">/g" $RUNWAY_WORKSPACE/runwaysdk-test/src/main/resources/log4j2.xml

# Configure properties files
RUN echo "appcfg=dev\nproject.basedir=$RUNWAY_WORKSPACE\nmaven.m2=/root/.m2" > $RUNWAY_WORKSPACE/envcfg/envcfg.properties

# Generate class files
RUN mvn clean install

# Set the local classpath
WORKDIR $RUNWAY_WORKSPACE/runwaysdk-test
RUN mvn dependency:build-classpath -Dmdep.pathSeparator=";" -Dmdep.outputFile=$RUNWAY_WORKSPACE/runwaysdk-test/target/build-classpath.out
RUN LOCAL_CLASSPATH=$(cat $RUNWAY_WORKSPACE/runwaysdk-test/target/build-classpath.out) && ESCAPED_LOCAL_CLASSPATH=$(printf '%s\n' "$LOCAL_CLASSPATH" | sed -e 's/[]\/$*.^[]/\\&/g') && sed -i -e "s|local\.classpath=.*|local.classpath=$ESCAPED_LOCAL_CLASSPATH|g" $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk/common.properties
WORKDIR $RUNWAY_WORKSPACE

RUN mkdir $RUNWAY_WORKSPACE/bin
RUN wget -nv -O $RUNWAY_WORKSPACE/bin/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
RUN chmod +x $RUNWAY_WORKSPACE/bin/wait-for-it.sh

# The tests are split up like this because otherwise it runs out of memory (memory leak?)
CMD $RUNWAY_WORKSPACE/bin/wait-for-it.sh -t 60 $POSTGRES_HOST:$POSTGRES_PORT -- \
  && cd runwaysdk-test && mvn process-resources -P build-database -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Droot.clean=true -Dpatch=false \
  && ($DATA_ACCESS_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/DataAccessTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($BUSINESS_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/BusinessTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($FACADE_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/FacadeTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($VAULT_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/VaultTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($QUERY_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/QueryTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($MULTITHREADED_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/MultiThreadTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($ONTOLOGY_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/OntologyTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($SESSION_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/SessionTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true); \
  ($GRAPH_TEST_SUITE && mvn test -DforkCount=$MAVEN_TEST_FORK_COUNT -Dtest="com/runwaysdk/test/GraphTS.java" -Dorientdb.db.url=$ORIENTDB_HOST -Ddatabase.hostURL=$POSTGRES_HOST -Ddatabase.port=$POSTGRES_PORT -Drunway.keepSource=false -Drunway.keepBaseSource=false -Drunway.session.allowSystemLogin=true)
