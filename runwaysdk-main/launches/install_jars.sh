WKSPACE="/users/terraframe/documents/workspace/Runway-SDK"

mvn install:install-file \
-Dfile="${WKSPACE}/runwaysdk-client/lib/fileupload-progress.jar" \
-DgroupId=com.scand.fileupload \
-DartifactId=fileupload-progress \
-Dversion=1.0 \
-Dpackaging=jar \
-DgeneratePom=true

mvn install:install-file \
-Dfile="${WKSPACE}/runwaysdk-common/lib/UTF8Resource.jar" \
-DgroupId=com.terraframe \
-DartifactId=UTF8Resource \
-Dversion=1.0 \
-Dpackaging=jar \
-DgeneratePom=true

mvn install:install-file \
-Dfile="${WKSPACE}/runwaysdk-server/lib/ojdbc14.jar" \
-DgroupId=com.oracle \
-DartifactId=ojdbc14 \
-Dversion=1.2 \
-Dpackaging=jar \
-DgeneratePom=true