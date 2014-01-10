POC_liquibase
=============

A sample project to showcase how we intend to integrate with liquibase in our projects. The requirements below have
been given priority:

A. To provide DBA's freedom in their designs, see http://blog.aparnachaudhary.com/2013/08/03/database-change-management-with-liquibase/
  
B. Database structure changes to be visible to all team members.

  There are database versioning tools rely on the manual creation of SQL or SQL-like changesets, 
  but many use a simplistic tracking system that does not scale to multiple developers or code branches. 
  In particular, they are built around a concept of a linear database "version" which starts at version 1. 
  After a change is added, the version is incremented to 2, then 3, etc. When an existing database is set 
  to be updated, the current version is determined and all the changesets after that version are applied.
  This works well for projects where only one person adds changesets and/or there are never any branches, 
  ut it quickly breaks down when separate developers attempt to add a "version 4" to the database concurrently. 
  This can be managed with good team communication, but falls apart completely when versions 4 to 10 were added 
  in a feature branch that needs to be integrated back into the master branch that already has versions 4 to 20 added.
  Liquibase does away with this issue by using a unique identification scheme for each changeset that is 
  designed to guarantee uniqueness across developers and branches while still being easy to manually manage. 
  As you will see in the below examples, each Liquibase changeset contains two attributes: an "id" and an "author". 
  These two attributes along with the name and path of the file make up the changeset identifier Liquibase 
  uses to determine if it has been ran against a given database. At update time, each changeset is compared 
  against the list of applied changesets and it is executed if and only if it has not been run before. 
  Since the comparison is done for each changeset instead of being based on a single "version", any new 
  changesets brought into the changelog file - whether from a different developer or from a different 
  branch - will be correctly executed.


Howtos
++++++

Install
-------

wget http://ufpr.dl.sourceforge.net/project/liquibase/Liquibase%20Core/liquibase-3.0.8-bin.tar.gz
tar -xzvf liquibase-3.0.8-bin.tar.gz

Set LIQUIBASE_HOME. Preferably a permanent environment variable.
----------------------------------------------------------------

export LIQUIBASE_HOME=~/liquibase-3.0.8-bin

Add the mysql database driver permanently to the liquibase classpath. FlexiFin preferes this database.
------------------------------------------------------------------------------------------------------

cp ~/.m2/repository/mysql/mysql-connector-java/5.1.28/mysql-connector-java-5.1.28.jar $LIQUIBASE_HOME/lib/

Test by creating a master changelog for the masterdata database.
----------------------------------------------------------------

liquibase --driver=com.mysql.jdbc.Driver \
    --url="jdbc:mysql://localhost/masterdata?characterEncoding=utf8" \
    --username=flexifin  --password=flexifin \
    --changeLogFile=db.changelog-master.xml \
    generateChangeLog
