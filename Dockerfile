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

ENV POSTGRES_HOST="localhost:5432"
ENV RUNWAY_WORKSPACE=/runwaysdk

ENV MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError"

RUN mkdir $RUNWAY_WORKSPACE
WORKDIR $RUNWAY_WORKSPACE

# Copy the source in
COPY . $RUNWAY_WORKSPACE

RUN ls $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk

# Log4j properties
RUN wget -nv -O $RUNWAY_WORKSPACE/runwaysdk-test/src/main/resources/log4j2.xml https://raw.githubusercontent.com/terraframe/geoprism-cloud/dev/ansible/roles/webserver/files/log4j2.xml
RUN sed -i -e 's/<Root level="error">/<Root level="$LOG_LEVEL">/g' $RUNWAY_WORKSPACE/runwaysdk-test/src/main/resources/log4j2.xml

# Set pathing in properties files
RUN sed -i -e 's|${project.basedir}|$RUNWAY_WORKSPACE|g' $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk/common.properties
RUN sed -i -e 's|${project.basedir}|$RUNWAY_WORKSPACE|g' $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk/server.properties
RUN sed -i -e 's|${project.basedir}|$RUNWAY_WORKSPACE|g' $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk/runwaygis.properties

# Generate class files
RUN mvn clean install

# Set the local classpath
WORKDIR $RUNWAY_WORKSPACE/runwaysdk-test
RUN mvn dependency:build-classpath -Dmdep.outputFile=$RUNWAY_WORKSPACE/runwaysdk-test/target/build-classpath.out
RUN export LOCAL_CLASSPATH=$(cat $RUNWAY_WORKSPACE/runwaysdk-test/target/build-classpath.out)
RUN echo $LOCAL_CLASSPATH
RUN sed -i -e 's|local\.classpath=.*|local.classpath=$LOCAL_CLASSPATH|g' $RUNWAY_WORKSPACE/envcfg/dev/runwaysdk/common.properties

WORKDIR $RUNWAY_WORKSPACE

RUN mkdir $RUNWAY_WORKSPACE/bin
RUN wget -nv -O $RUNWAY_WORKSPACE/bin/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
RUN chmod +x $RUNWAY_WORKSPACE/bin/wait-for-it.sh

CMD $RUNWAY_WORKSPACE/bin/wait-for-it.sh -t 60 $POSTGRES_HOST -- && cd runwaysdk-test && mvn install -P build-database -Droot.clean=true -Dpatch=false && mvn test -Dtest="com/runwaysdk/test/UeberTS.java" -Drunway.keepSource=false -Drunway.keepBaseSource=false
