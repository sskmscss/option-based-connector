### Install nClientJar with the below mentioned command
mvn install:install-file -Dfile=common/lib/nClient.jar -DgroupId=com.pcbsys.nirvana.client -DartifactId=nClient -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true


### Publish the Message
## FOR UM PUBLISH
curl -X POST -d "{\"shipmentId\" : \"1234\"}" -H "Content-type: text/json" http://localhost:8099/api/postEvents/um

## FOR KAFKA PUBLISH#
curl -X POST -d "{'test2': 'test2'}" -H "Content-type: text/json"  http://localhost:8099/api/postEvents/kafka

## NOT REQUIRED - Subscribe the Message - In future any issue or if we need a end point for subscription we can use this one
curl -X GET -H "Content-type: text/json"  http://localhost:8099/api/getEvents/um
curl -X GET -H "Content-type: text/json"  http://localhost:8099/api/getEvents/kafka
