#
# Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

# if curl -f -s --head "http://nexus.terraframe.com/service/local/artifact/maven/redirect?r=allrepos&g=com.runwaysdk&a=runwaysdk-server&p=jar&v=$VERSION" | head -n 1 | grep "HTTP/1.[01] [23].." > /dev/null; then
#    echo "The release version $VERSION has already been deployed! Please ensure you are releasing the correct version of runway."
#    exit 1
# fi

git config --global user.name "$GIT_TF_BUILDER_USERNAME"
git config --global user.email builder@terraframe.com

cd $WORKSPACE/runway-sdk

#git checkout dev
#git pull

#mvn license:format
#git add -A
#git diff-index --quiet HEAD || git commit -m 'License headers'
#git push

#git checkout master
#git merge dev
#git push


mvn release:prepare -B -Dtag=$VERSION \
                 -DreleaseVersion=$VERSION \
                 -DdevelopmentVersion=$NEXT
                 
mvn release:perform -Darguments="-Dmaven.javadoc.skip=true -Dmaven.site.skip=true"


#cd ..
#rm -rf rwdev
#mkdir rwdev
#cd rwdev
#git clone -b master git@github.com:terraframe/Runway-SDK.git
#cd Runway-SDK
#git checkout dev
#git merge master
#git push