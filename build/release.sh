# if curl -f -s --head "http://nexus.terraframe.com/service/local/artifact/maven/redirect?r=allrepos&g=com.runwaysdk&a=runwaysdk-server&p=jar&v=$VERSION" | head -n 1 | grep "HTTP/1.[01] [23].." > /dev/null; then
#    echo "The release version $VERSION has already been deployed! Please ensure you are releasing the correct version of runway."
#    exit 1
# fi

git config --global user.name "$GIT_TF_BUILDER_USERNAME"
git config --global user.email builder@terraframe.com

cd $WORKSPACE/runway-sdk

git checkout dev
git pull

mvn license:format
git add -A
git diff-index --quiet HEAD || git commit -m 'License headers'
git push

git checkout master
git merge dev
git push


mvn release:prepare -B -Dtag=$VERSION \
                 -DreleaseVersion=$VERSION \
                 -DdevelopmentVersion=$NEXT
                 
mvn release:perform -Darguments="-Dmaven.javadoc.skip=true -Dmaven.site.skip=true"


cd ..
rm -rf rwdev
mkdir rwdev
cd rwdev
git clone -b master git@github.com:terraframe/Runway-SDK.git
cd Runway-SDK
git checkout dev
git merge master
git push