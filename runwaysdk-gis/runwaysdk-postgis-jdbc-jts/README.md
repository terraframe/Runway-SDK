# What is this?

There is a bug in the dependency:
<groupId>net.postgis</groupId>
<artifactId>postgis-jdbc-jts</artifactId>
<version>2021.1.0</version>

Which is fully described here:
https://github.com/postgis/postgis-java/issues/125

This project repackages the jar WITHOUT the `META-INF/services/java.sql.Driver`, which resolves the issue for our purposes.
