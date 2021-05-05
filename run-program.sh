#!/bin/sh
arg1=$1
echo "executing mvn clean package -Dmaven.test.skip=true"
mvn clean package -Dmaven.test.skip=true
if [ $? -eq 0 ]; then
    echo "------------------------------------------------------------------------"
    echo "Packaged target/ValetParkingApplication-1.0-SNAPSHOT.jar successfully."
    echo "------------------------------------------------------------------------"
    echo "Executing Program ValetParking with sample: src/main/resources/input-data.txt"
    echo "\"java -jar target/ValetParkingApplication-1.0-SNAPSHOT.jar src/main/resources/input-data.txt\"\n"
    if [ -z "$1" ]; then
        java -jar target/ValetParkingApplication-1.0-SNAPSHOT.jar src/main/resources/input-data.txt
        echo "------------------------------------------------------------------------"
        echo "to execute Junit tests, execute command \"mvn test\""
        echo "------------------------------------------------------------------------"
        exit 1
    else
        java -jar target/ValetParkingApplication-1.0-SNAPSHOT.jar $arg1
    fi
else
    echo "Failed, please try running from cli"
fi
