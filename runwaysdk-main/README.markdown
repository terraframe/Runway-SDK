Runway-SDK
=======

Required Eclipse plugins for a Runway developer:

* Egit
* AJDT
* M2e - http://eclipse.org/m2e/
* Maven Integration for AJDT - http://dist.springsource.org/release/AJDT/configurator/


How to compile Runway jars
-------
1. Run [main] mvn clean install.launch in runwaysdk-main/launches


How to deploy Runway jars to a directory
-------
1. Compile the Runway jars (mvn install)
2. Run the ant copy launch
