Runwaysdk Test
=======


How to run Runway's Tests (through Eclipse):
-------
1. In Eclipse execute a clean and build of all projects.
2. Run [test] mvn exec;java (rebuild database).launch (in runwaysdk-test/launches/maven)
3. Run mvn dependency:copy-dependencies ... launch config (lib hack)
4. Junit Run UeberTestSuite.launch


* The rebuild database script reads the properties files from the target directory. If you change a profile you MUST do a build before you launch the rebuild database script or it will use the stale values.
* Number 3 (The lib hack) only ever needs to be executed once.



How to run Runway's Tests (using the Maven CLI):
-------
1. Run [test] mvn clean test.launch
