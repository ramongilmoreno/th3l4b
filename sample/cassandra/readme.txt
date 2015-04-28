The following commands allows starting a Docker-based Cassandra process that
can be used by the com.th3l4b.srm.sample.cassandra.junit.CassandraTest class
main function.

For debugging purposes, as Eclipse does not allow to run the JUnit
cassandra-unit test properly, probably due to a classpath problem (Gradle
defines test classpath properly, but Eclipse does not ignore dependencies
as much as Gradle does).

The commands are:

    docker-cassandra-start.sh
    docker-cassandra-stop.sh

Commands need to be run with sudo typically. This is an snippet of an execution:

    $ sudo ./docker-cassandra-start.sh 
    [sudo] password for johndoe: 
    db6220bee6be6b51b93935709ac832ecf726b04269154fe4bdde607682ffb814
    $ sudo ./docker-cassandra-stop.sh 
    th3l4bsrmcassandratest
    th3l4bsrmcassandratest
    $ sudo docker ps -a
    CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
    $ 

