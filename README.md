# Valet Parking
Valet Parking is Car & Motorcycle parking application. The program accepts file as an input, allocated specified parking lot and processes Enter / Exit events.

## Installation & Execution
### Project Requirements
```
- JDK 1.8 : "sudo apt install openjdk-8-jdk"
- Maven 3.6 or above https://maven.apache.org/install.html
```
### Manual Execution via cli
```
1. Switch to root folder or the project & ensure pom.xml is present in current folder
- ls -l pom.xml
-rw-r--r--  1 dipeshshah  staff  2074 May  5 16:46 pom.xml

2. run command mvn to cleanup and package the jar file
mvn clean package

3. to execute JunitTests execute follow command
mvn test

4. Ensure the maven build is successful and jar file is created
ls -ls target/ValetParkingApplication-1.0-SNAPSHOT.jar
2184 -rw-r--r--  1 dipeshshah  staff  1116054 May  5 17:51 target/ValetParkingApplication-1.0-SNAPSHOT.jar

5. run program as follows with given sample file
java -jar target/ValetParkingApplication-1.0-SNAPSHOT.jar src/main/resources/input-data.txt
Accept MotorcycleLot1
Accept CarLot1
MotorcycleLot1 2
Accept CarLot2
Accept CarLot3
Reject
CarLot3 6
```

### Steps to enable debug Logging
```
You can enable debug level logging by changing configuration specified in "src/main/resources/logback-test.xml" 
change <root level="info"> => <root level="debug"> and save the file, rerun above steps to execute program.
```

### Execution via shell script
```
1. Ensure you are in root folder of the project and shell script is visible
ls -ls run-program.sh
8 -rwxr-xr-x@ 1 dipeshshah  staff  333 May  5 17:16 run-program.sh

2. Change permission to execute script if permission its not available
chmod 755 ./run-program.sh

3. run program with given sample file as follows
$ ./run-program.sh
executing mvn -q clean package
executing junit Tests
1 2 3
Accept CarLot1
Accept CarLot1
CarLot1 2
Accept MotorcycleLot1
MotorcycleLot1 1
Done
=================================================================

Executing Program ValetParking

Accept MotorcycleLot1
Accept CarLot1
MotorcycleLot1 2
Accept CarLot2
Accept CarLot3
Reject
CarLot3 6

4. To run program with another sample file you can run the script as follows, where 2nd parameter is desired filename

./run-program.sh <file-path>
./run-program.sh src/test/resources/input-big-3.txt
```

### Execution of Sample file via Docker
```
1. Ensure docker is installed on system
for Ubuntu installation follow: https://docs.docker.com/engine/install/ubuntu/#installation-methods

2. Execute ./run-docker.sh
Note: the docker execution is currently only limited to sample file. Dockerfile will be updated to support file-path parameter in future.
```

##    Assumptions & Notes:

1. The given list is in sorted order of events for given vehicle with license number, Vehicle must enter into parking lot on timestamp x and may leave on y timestamp where y >= x;

2. Assuming event list has historic data, events in the epoch time range of ( 0 to LONG.MAX_VALUE ) are supported.

3. A vehicle can't "Enter" into parking lot more than once before exiting i.e The parking ticket is calculated based on subsequent "Exit" event followed by "Enter" from the given list. The program will throw an exception if the vehicle entered more than once before exiting. 

4. A vehicle can't "Exit" from parking lot more than once before entering again i.e The parking ticket is calculated based on subsequent "Enter" and "Exit" from the given list. The program will throw an exception if the vehicle exited before Entering.

5. If the vehicle "Exit" event timestamp <= "Enter" event timestamp, we will remove car from parking but return 0 parking ticket charge.

6. The program is designed to handle maximum INTEGER.MAX_VALUE parking slots i.e 2147483647 per vehicle type.  

7. The program assumes 1st line of every input file is capacity of Vehicle Lot and following lines only have "Enter" and "Exit" events.

8. Program will throw an error/exception if the event is not recognized. The program can be extended to support more events and also the comparison of event command can be relaxed however events must be strongly typed i.e only "Enter" and "Exit" events from file will be recognized for this version of the submission.

9. If program encounters unrecognized event then it will throw an error/Exception and it will continue processing other subsequent events.

10. Program strictly expect filepath as an argument, It will throw an error if no file is passed or more than 1 file are passed.

11. Input file must contain space between parking capacity, Each event in file should be separated by new line.

## Project Structure & Notes

```
.
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── zendesk : Main entrypoint for the project
    │   │           └── valetparking
    │   │               ├── constants : Containing Supported Event Type, Capacity etc
    │   │               ├── dao : DS to store data after processing file events
    │   │               ├── entity : Classes Vehicle, Car, Motorcycle, VehicleType
    │   │               ├── exception: Custom project specific Exception classes
    │   │               ├── service : Service to handle and process events from file
    │   │               └── validator : Validate events before storing and processing
    │   └── resources   : sample-input file
    └── test
        ├── java
        │   └── com
        │       └── zendesk
        │           └── ValetParking
        │               └── test  : junit testcases for classes: dao, service & validator
        └── resources   : multiple test-input files
```

## Contributing

@author: dipesh shah
@email: dipeshlshah@gmail.com