Runwaysdk GIS
=======

How to run Runwaysdk's GIS Tests (through Eclipse):
-------
1. Run [gis] mvn clean install to build & jar the GIS projects
2. Run [gis-test] mvn exec;java -Drebuild-database.launch
3. Run [gis-test] mvn exec;java -Dimport-schema.launch
4. Right click on the .java for the test Run as->Junit Test
