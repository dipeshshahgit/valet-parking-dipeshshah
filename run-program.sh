#!/bin/sh
arg1=$1
echo "executing mvn -q clean package"
echo "executing junit Tests"
~/Downloads/apache-maven-3.6.3/bin/mvn -q clean package
echo "Done";
echo "=================================================================";
echo "\nExecuting Program ValetParking\n"
if [ -z "$1" ]; then
        java -jar target/ValetParkingApplication-1.0-SNAPSHOP.jar src/main/resources/input-data.txt
        exit 1
else
        java -jar target/ValetParkingApplication-1.0-SNAPSHOP.jar $arg1
fi
