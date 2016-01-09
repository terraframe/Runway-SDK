set -x #echo on

# Install Oracle JDK
wget -O jdk-8u60-linux-x64.rpm "http://nexus.terraframe.com/service/local/artifact/maven/redirect?r=releases&g=com.oracle&a=jdk-linux-amd64&v=8u60&e=rpm"
rpm --erase --nodeps java-1.8.0-openjdk-1.8.0.51-1.b16.6.amzn1.x86_64 java-1.8.0-openjdk-headless-1.8.0.51-1.b16.6.amzn1.x86_64
rpm -Uvh jdk-8u60-linux-x64.rpm
/usr/sbin/alternatives --install /usr/bin/java java /usr/java/default/bin/java 3
/usr/sbin/alternatives --set java /usr/java/default/bin/java
/usr/sbin/alternatives --install /usr/bin/java_sdk java_sdk /usr/java/default/bin/java 3
/usr/sbin/alternatives --set java_sdk /usr/java/default/bin/java

mkdir /usr/share/jenkins
sudo chmod 777 /usr
sudo chmod 777 /usr/share
sudo chmod 777 /usr/share/jenkins

sudo yum -y install git

echo "-----BEGIN RSA PRIVATE KEY-----
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
-----END RSA PRIVATE KEY-----" > /home/ec2-user/.ssh/id_rsa
chmod 700 /home/ec2-user/.ssh/id_rsa
chown ec2-user:ec2-user /home/ec2-user/.ssh/id_rsa

echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDAzbffVG90QGelbVmvpW5sQDcg6sVUpVFfam2npNinxl6wDA3aLJGP9sgJyX8IFgL+mdR1RoeEWaLMmNAk0z66/tLFXooOUxEWxZ6qyOqaoJzyKmBKteGFoIcMlqLXVlcQLtRAdlR1W64nGCQJPfMrL5rGSR3vQ9K0wFjILG9uTH3OtMELlDntJlKwhCibhBLoJH1oPxZPAfiWBplVxrQwHKKia2wK//pNjMaXhqVd78kg5LVvlgN/SKZk2RznxTcW7idMnvGCwV2Epu33+5qHOPEgrVhG2iepd/RvZdKzahS3iD7q6Dwjahz3NZaNbtQUW6sAGDK0S/E0BvDhAl7uWC6vvtlrh4X7vxO+RD7/iunOdCjvos2R+ibisGwC72ZpTd/c1k9DSLddSkb4Bg4+RkzRd3OnSy0Uh1UabM1XQoxgaxALZ75mcRQfQeR/E2DFzY2KV1UQX+5bCUUk+Z9vGj1uFHQ7eJ02zO6FnQWwdTb5aRP72c8CNlb7vvos0Y4uSsPjUgnaKyt6hiWqY4Njt7TV8D4eXHu+YlAzG0Om8ZTZPNzzJYtTQVFIehsDp/xvx57SaavuDZywHaUIM+fM6eM8J8lW4q+sNsDYOOCQzfuQBDjDyf9fpkVRGON1SBedkFMo3Bfraz9LkXvZTcFhXas4V99yoloZFoLi2It+Cw== runwayci@terraframe.com" > /home/ec2-user/.ssh/id_rsa.pub
chmod 700 /home/ec2-user/.ssh/id_rsa.pub
chown ec2-user:ec2-user /home/ec2-user/.ssh/id_rsa.pub

ssh-keyscan -t rsa github.com >> /home/ec2-user/.ssh/known_hosts
chmod 700 /home/ec2-user/.ssh/known_hosts
chown ec2-user:ec2-user /home/ec2-user/.ssh/known_hosts

wget ftp://mirror.reverse.net/pub/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
tar xvf apache-maven-3.3.9-bin.tar.gz
sudo mv apache-maven-3.3.9  /usr/local/apache-maven
export M2_HOME=/usr/local/apache-maven
export M2=$M2_HOME/bin 
export PATH=$M2:$PATH
source /home/ec2-user/.bashrc
mvn -version

# Install postgres
sudo yum -y install postgresql93-server postgresql93-devel postgresql93-contrib
sudo service postgresql93 initdb
sudo sed -i 's/ident/trust/g' /var/lib/pgsql93/data/pg_hba.conf
sudo sed -i 's/peer/trust/g' /var/lib/pgsql93/data/pg_hba.conf
sudo service postgresql93 start
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

# Install postgis
  #based on http://imperialwicket.com/aws-configuring-a-geo-spatial-stack-in-amazon-linux
  #Set up PostGIS 2.0 - Have to compile it. Not in Amazon's yum repo and I don't want to hack RPM
  # First install gcc and friends
  sudo yum -y install gcc make gcc-c++ libtool libxml2-devel

  # make a directory for building
  cd /home/ec2-user/
  mkdir postgis 
  cd postgis

  # download, configure, make, install geos (GEOS 3.3.2+ is recommended.)
  wget http://download.osgeo.org/geos/geos-3.4.2.tar.bz2
  tar xjf geos-3.4.2.tar.bz2
  cd geos-3.4.2
  ./configure
  make
  sudo make install

  # download, configure, make, install proj (version 4.6.0 or greater)
  cd /home/ec2-user/postgis/
  wget http://download.osgeo.org/proj/proj-4.8.0.tar.gz
  wget http://download.osgeo.org/proj/proj-datumgrid-1.5.zip
  tar xzf proj-4.8.0.tar.gz
  cd proj-4.8.0/nad
  unzip ../../proj-datumgrid-1.5.zip
  cd ..
  ./configure  
  make
  sudo make install

  # download, configure, make, install postgis 2.x
  cd /home/ec2-user/postgis/
  wget http://download.osgeo.org/postgis/source/postgis-2.1.5.tar.gz
  tar xzf postgis-2.1.5.tar.gz 
  cd postgis-2.1.5
  ./configure --with-geosconfig=/usr/local/bin/geos-config --without-raster
  make
  sudo make install

  # update your libraries
  sudo echo /usr/local/lib >> /etc/ld.so.conf
  sudo ldconfig

sudo -u postgres createdb template_postgis
sudo -u postgres createlang plpgsql template_postgis
sudo -u postgres psql -d template_postgis -f /usr/share/pgsql93/contrib/postgis-2.1/postgis.sql
sudo -u postgres psql -d template_postgis -f /usr/share/pgsql93/contrib/postgis-2.1/spatial_ref_sys.sql