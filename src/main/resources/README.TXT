#How to install.
wget http://ufpr.dl.sourceforge.net/project/liquibase/Liquibase%20Core/liquibase-3.0.8-bin.tar.gz
tar -xzvf liquibase-3.0.8-bin.tar.gz

#Set LIQUIBASE_HOME. Preferably a permanent environment variable.
export LIQUIBASE_HOME=~/liquibase-3.0.8-bin

#Add the mysql database driver permanently to the liquibase classpath. FlexiFin preferes this database.
cp ~/.m2/repository/mysql/mysql-connector-java/5.1.28/mysql-connector-java-5.1.28.jar $LIQUIBASE_HOME/lib/

#Test by creating a master changelog for the masterdata database.
liquibase --driver=com.mysql.jdbc.Driver \
    --url="jdbc:mysql://localhost/masterdata?characterEncoding=utf8" \
    --username=flexifin  --password=flexifin \
    --changeLogFile=db.changelog-master.xml \
    generateChangeLog