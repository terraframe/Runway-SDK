1. Install Eclipse plugins:

   * Aspect J Development Tools (via Help->Eclipse Marketplace)
   * Maven Integration for Eclipse WTP, m2e-wtp (via Help->Eclipse Marketplace)
   * Maven Integration for AJDT (Help->Install New Software : http://dist.springsource.org/release/AJDT/configurator/)
   * M2E connector, buildhelper (Mac: Eclipse->Preferences | Windows: Window -> Preferences) -> Maven -> Discovery -> Open Catalog. Search for buildhelper (by Sonatype).


2. Install PostgreSQL
   * Runwaysdk assumes your database root credentials are postgres::postgres (user::pass). You can change this in your project pom.xml file.
   * Runwaysdk connects to your database via port 5432. You can change this via Java Resources->src/main/resources/default/server/database.properties


3. Build the project, then rebuild the database and install the schemas (launches)


4. Install a local copy of tomcat 6 and then copy the runwaySDKTomcatLoader.jar from tomcat-lib to your tomcat lib directory.


5. Right click on the project, Run As->Run on Server
