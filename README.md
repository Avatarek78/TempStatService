# TempStatService
The service for statistics temperature data. 
Service allows the user to store records from temperature measurements. 
Each record about measurement contains:
- unique id of measurement
- temperature in Celsius degree as decimal number (e.g. 18.548)
- date and time of measurement in format "yyyy-MM-dd HH:mm:ss.SSS"


User can:
- store new Temperature
- get Temperature by id
- update Temperature data by id
- delete Temperature by id
- get all Temperatures
- get Temperatures by temp value range
- get Temperatures for time period
- get longest time period by temp value range
- get longest time period by temp value range and hour range of day

## API documentation in Apiary 
https://tempstatservice.docs.apiary.io

## API documentation in Postman 
https://documenter.getpostman.com/view/6819917/S17utnG2 

## Docker
- MySQL is available on port 3406
- TempStatService is available on port 8080
#### Run application in docker container
Before run please build application by command "mvn clean install".
All Docker files is in docker folder. You can run Docker container from this folder like this:

Linux: 
- run "docker-compose up"

Windows:
- requires active virtualization supported by Vagrant (VirtualBox, VMware, Hyper-V, ...)  
- run "vagrant up"
