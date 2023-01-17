# Trading Engine App

    A simple java based code for creating orderbook, sending orders and producing trades.

## how do I run it?
Before Installation , you have to install java11 and maven in your machine.
* note : There are some TestCases that you can run with maven : 
```shell 
./mvnw test  
or :
mvn test
```
* Clean install

```shell
./mvnw clean install 
 or 
 mvn clean install
 cd target  
```

* Run app

```shell
java -jar TradingEngine-1.0-SNAPSHOT.jar   
```

## How it works:

    When running the jar file, the application will be waiting for input.
    You can give line by line and also many lines as input.

## Example

``` text
    input:
    
    10000,B,98,25500
    10005,S,105,20000
    10001,S,100,500
    10002,S,100,10000
    10003,B,99,50000
    10004,S,103,100
    
    output:
    Buyers      Sellers
    50000       99     | 100    500        
    25500       98     | 100    10000      
                       | 103    100        
                       | 105    20000      

    * Note: You have to Enter an empty line after writing all orders.
    
