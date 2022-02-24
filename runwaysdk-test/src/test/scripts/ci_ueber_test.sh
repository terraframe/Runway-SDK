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

# This build script is designed to be run by a Hudson continuous integration framework.

cd ..
rm -r workspace
mkdir workspace
chmod 777 workspace
cd workspace
git clone git@github.com:terraframe/Runway-SDK.git .
git config user.name Hudson
git config user.email hudson@terraframe.com
cd ..
chown tomcat:tomcat -R workspace
cd workspace

:
: ----------------------------------
:   BUILD    -   Building Runway   
: ----------------------------------
:
/usr/local/apache-maven-3.0.5/bin/mvn clean install

:
: ----------------------------------
:  TEST - Running Ueber Test Suite  
: ----------------------------------
:
cd runwaysdk-test
/usr/local/apache-maven-3.0.5/bin/mvn clean test -P rebuild-database -Drunway.keepSource=false -Drunway.keepBaseSource=false
/usr/local/apache-maven-3.0.5/bin/mvn clean initialize -P clean-gen
cd ..

:
: -----------------------------------------
: DEPLOY SITE - Deploying jars, making site
: -----------------------------------------
:
# We're going to skip deploying jars, at least until we're doing legit runway versioning
# /usr/local/apache-maven-3.0.5/bin/mvn clean deploy
/usr/local/apache-maven-3.0.5/bin/mvn clean site


# /usr/local/apache-maven-3.0.5/bin/mvn clean deploy site

:
: ----------------------------------
:      JAVADOC - Creating javadoc
: ----------------------------------
:
/usr/local/apache-maven-3.0.5/bin/mvn javadoc:aggregate
rm -r $CATALINA_HOME/webapps/javadoc
cp -r ./target/site/apidocs $CATALINA_HOME/webapps/javadoc