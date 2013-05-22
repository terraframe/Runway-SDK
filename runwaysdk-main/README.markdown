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


How to deploy Runway jars to Terraframe's Nexus Maven Server
-------

Deploying jars to Terraframe's Nexus server requires authentication. Ask a Terraframe employee for the password and input it into this XML:

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
      http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <servers>
            <server>
                <id>terraframe-releases</id>
                <username>admin</username>
                <password>ASK_TERRAFRAME_FOR_PASSWORD</password>
            </server>
            <server>
                <id>terraframe-snapshots</id>
                <username>admin</username>
                <password>ASK_TERRAFRAME_FOR_PASSWORD</password>
            </server>
        </servers>
    </settings>

Place this XML in a new file located at ~/.m2/settings.xml.