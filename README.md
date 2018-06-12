#ATMDemo

Author : John McKeogh

The following contains source for a basic Restful interface to an ATM.

To Run the service:
cd $(PROJECT_ROOT)/rest
mvn spring-boot:run


On initialisation the service is loaded with the data in rest/src/main/resources/data.sql

Account information
AcNumber   PIN   Balance   Overdraft
123456789  1234  800       200
987654321  4321  1230      150

ATM Loaded
CASH VALUE			5     10    20   50
NUMBER OF NOTES     20    30    30   20


Example restful invocations:
1) Get Account Data For 123456789
curl -H "Content-Type: application/json" -X  GET http://localhost:8080/123456789?pin=1234

{"accountNumber":123456789,"pin":1234,"balance":800,"overdraft":200}

2) Get Balance for 123456789
curl -H "Content-Type: application/json" -X  GET http://localhost:8080/123456789/balance?pin=1234

{"balance":800}

3) Get Max withdrawal
curl -H "Content-Type: application/json" -X  GET http://localhost:8080/123456789/withdrawlimit?pin=1234

{"limit":1000}

4) Dispense cash €285
curl -H "Content-Type: application/json" -X  POST http://localhost:8080/123456789/dispense/285?pin=1234

{"50":5,"20":1,"5":1,"10":1}

