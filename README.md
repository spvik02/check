The program caculate and print a shop receipt with the specified data in the parameters

Java 21, gradle 8.5

to build: ./gradlew clean build

to run: java -jar build/libs/check-1.0-SNAPSHOT.jar <set_of_parameters>

Where the set of parameters in the format 
* itemId-quantity (ItemId is the identifier of the product, quantity is its quantity),
* discountCard=xxxx if discount card was provided.
* balanceDebitCard=xxxx. balanceDebitCard parameter is required.

For example: java -jar build/libs/check-1.0-SNAPSHOT.jar 1-2 2-5 2-6 balanceDebitCard=100 
![image](https://github.com/spvik02/check/assets/111181469/9da187a7-8288-449d-b068-c41fa3b2dd99)

For error example: java -jar build/libs/check-1.0-SNAPSHOT.jar 1-2 2-5 2-6 balanceDebitCard=10
![image](https://github.com/spvik02/check/assets/111181469/0ac76f96-c260-4496-8a35-ec5c75e03224)

