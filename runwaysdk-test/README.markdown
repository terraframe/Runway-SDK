Runwaysdk Test
=======


How to run Runway's Tests (through Eclipse):
-------
1. Install your runway source with maven if you need to
2. Run [test] database clean install (in runwaysdk-test/launches/maven)
3. Set your local.classpath in common.properties
  a. Set local.classpath to ${maven.compile.classpath}
  b. Run runwaysdk-test as process-resources maven goal
  c. Open common.properties in target. Copy the compiled local.classpath value into common.properties in src/main/resources
4. Build all the runway projects with Eclipse
