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

# This script will start a local docker instance that you can use for testing and development
# Run it with sudo


### Sensitive information (you need to put the values in here yourself) ###
export AWS_ACCESS_KEY_ID=XXXXXX && export AWS_SECRET_ACCESS_KEY=XXXXXX


docker rm -f $(docker ps -a -q --filter=name=solr)

set -e
set -x

BASEDIR=$(dirname "$0")


# Install preqreqs (assumes you're on Ubuntu!)
pip install --upgrade --user awscli
apt-get -y install tesseract-ocr libgdal-java
aws s3 cp s3://aip.geoprism.net/deployment-resources/Teigha.Java_lnxX64_4.4dll.tar.gz /tmp/staging/teigha/Teigha.Java_lnxX64_4.4dll.tar.gz
tar -xvzf /tmp/staging/teigha/Teigha.Java_lnxX64_4.4dll.tar.gz -C /tmp/staging/teigha
mkdir -p /usr/lib/jni
mv /tmp/staging/teigha/bin/lnxX64_4.4dll/* /usr/lib/jni/
rm -r /tmp/staging/teigha
export LD_LIBRARY_PATH="/usr/lib/jni"


docker run -d -p 8983:8983 --name solr solr:6
sleep 8

docker exec solr bash -c '/opt/solr/bin/solr create_core -c allpaths'

docker cp $BASEDIR/../solr-core/server 'solr:/opt/solr/server/../staging'
docker exec -u root solr bash -c 'cp -rf /opt/solr/server/../staging/* /opt/solr/server'
docker exec -u root solr bash -c 'rm -rf /opt/solr/server/../staging'        
docker cp $BASEDIR/../solr-core/allpaths 'solr:/opt/solr/server/solr/allpaths/../staging'
docker exec -u root solr bash -c 'cp -rf /opt/solr/server/solr/allpaths/../staging/* /opt/solr/server/solr/allpaths'
docker exec -u root solr bash -c 'rm -rf /opt/solr/server/solr/allpaths/../staging'
docker exec -u root solr bash -c 'chown -R solr:solr /opt/solr/server/solr/allpaths'

docker restart solr
sleep 8
docker logs solr

echo "Solr should now be running at http://127.0.0.1:8983 . Don't forget to run the indexer!"