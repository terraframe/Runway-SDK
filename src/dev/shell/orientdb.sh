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

# Run this with sudo
if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

export ORIENTDB_CONTAINER_NAME=runway-orientdb
export ORIENTDB_ROOT_PASS=root

# Exit immediately if anything errors out
set -ex

# Kill any running containers by name of what we're about to run
docker rm -f $(docker ps -a -q --filter="name=$ORIENTDB_CONTAINER_NAME") > /dev/null || true

# Pull & Run the orientdb container
docker run -d -p 2424:2424 -p 2481:2480 -e ORIENTDB_ROOT_PASSWORD=$ORIENTDB_ROOT_PASS -e ORIENTDB_OPTS_MEMORY="-Xms512M -Xmx2G" --name $ORIENTDB_CONTAINER_NAME orientdb:3.2
