# CRUD

Demo CRUD project for Axey FZCO

## Description

Simple REST API that allows to:

* add new employees (JSON)
* retrieve particular employee by UUID (JSON)
* retrieve list of all employees (JSON/CSV)
* update particular employee by UUID (JSON)
* delete particular employee by UUID (JSON)

## Requirements

1. JDK
2. Git
3. Docker Desktop

## Building and Running without Docker

1. `git clone https://github.com/wiktorkielar/crud.git`
2. `./mvnw clean install`
3. `java -jar target/crud-0.0.1-SNAPSHOT.jar`

## Running Unit Tests

1. `./mvnw test`

## Using the application

1. Please refer to the file in the main directory and import it to Postman: `Employees.postman_collection.json`
2. Alternatively use cURL
1. `curl --location 'http://localhost:8080/api/v1/json/employees' \
   --header 'Content-Type: application/json' \
   --data '{
   "uuid": "",
   "firstName": "John",
   "lastName": "Doe",
   "jobRole": "Java Developer"
   }'`
2. `curl --location 'http://localhost:8080/api/v1/json/employees/3c44cf86-d900-43aa-93f7-87f3db1f522d' \
   --data ''`
3. `curl --location 'http://localhost:8080/api/v1/json/employees' \
   --data ''`
4. `curl --location 'http://localhost:8080/api/v1/csv/employees' \
   --data ''`
5. `curl --location --request PUT 'http://localhost:8080/api/v1/json/employees' \
   --header 'Content-Type: application/json' \
   --data '{
   "uuid": "3c44cf86-d900-43aa-93f7-87f3db1f522d",
   "firstName": "Jaohn",
   "lastName": "Doe",
   "jobRole": "Java Developer"
   }'`
6. `curl --location --request DELETE 'http://localhost:8080/api/v1/json/employees/85b4ffcc-3274-4038-9a57-975c5dea98ce' \
   --data ''`