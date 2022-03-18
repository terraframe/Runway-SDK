
#### Not intended to be run! Intelligently pick and choose from examples in this file. #### 


## Building ##
docker build . -t runwaysdk-test


## Running ##

# Not Debug
docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=5442 -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError" --network=host runwaysdk-test

# Debug
docker run --name runwaysdk-test -v /data/runwaysdk-test:/runwaysdk/runwaysdk-test/target/surefire-reports --rm -e POSTGRES_PORT=5442 -e MAVEN_OPTS="-Xmx3500M -Xms3500M -XX:+HeapDumpOnOutOfMemoryError -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y" --network=host runwaysdk-test
