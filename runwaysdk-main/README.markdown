Runway-SDK
=======

Runway-SDK's Maven tasks are configured to connect to Terraframe's private FTP server to download required jars. Because this is Terraframe's private Maven FTP server, connections are restricted to users with valid login credentials. To use Runway-SDK, contact a Terraframe employee, retrieve the credentials, and then input them in the following XML:

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
  http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>terraframe-ftp</id>
            <username>terraframe</username>
            <password>ASK_TERRAFRAME_FOR_PASSWORD</password>
        </server>
        <server>
            <id>terraframe-ftp-snapshots</id>
            <username>terraframe</username>
            <password>ASK_TERRAFRAME_FOR_PASSWORD</password>
        </server>
    </servers>
</settings>

Place this XML in a new file located at ~/.m2/settings.xml.



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
