# order-producer-app

# Installation Instructions

# Pre-Requisites

1. Download and extract Kafka latest version from official website - https://kafka.apache.org/downloads
2. Run below commands to - <br/>
   start zookeeper<br/>
  ` $ bin/zookeeper-server-start.sh config/zookeeper.properties` <br/>
   
   start kafka server<br/>
   `$ bin/kafka-server-start.sh config/server.properties>` <br/>
   
   create topic<br/>
   `$ bin/kafka-topics.sh --create --topic order-submitted --bootstrap-server localhost:9092` <br/>
   
3. Run order-consumer-app at https://github.com/stjonnala009/order-consumer-app <br/>

4. Run 'OrderProducerAppApplication.kt' so that now order-producer-app server starts up and two endpoints '/place-order' and '/place-order/offer' will be now exposed  <br/>
      
      
      
      
   
  ### Sample Outputs
 ##### 1. When calling /place-order with json payload - { "items":["Apple","Apple","Orange","Apple" ] } <br/>
 &nbsp; &nbsp; &nbsp; /place-order output <br/>
          ![Alt text](src/main/resources/Outputs/1.png?raw=true "Optional Title") <br/>
 
 &nbsp; &nbsp; &nbsp; Order notification sent via order-consumer-app <br/>
          ![Alt text](src/main/resources/Outputs/2.png?raw=true "Optional Title") <br/>

 ##### 2.When calling /place-order/offer with json payload - { "items":["Apple","Apple","Orange","Orange","Orange" ] } <br/>
 &nbsp; &nbsp; &nbsp; /place-order/offer output <br/>
          ![Alt text](src/main/resources/Outputs/3.png?raw=true "Optional Title") <br/>

&nbsp; &nbsp; &nbsp; Order with offer notification sent via order-consumer-app <br/>
          ![Alt text](src/main/resources/Outputs/4.png?raw=true "Optional Title") <br/>

 ##### 3.When calling /place-order with exceeded stock limit- { "items":["Apple","Apple","Orange","Orange","Orange", "Orange", "Orange" ] } <br/>
&nbsp; &nbsp; &nbsp; /place-order output <br/>
         ![Alt text](src/main/resources/Outputs/5.png?raw=true "Optional Title") <br/>

&nbsp; &nbsp; &nbsp; Order out of stock notification sent via MailService console <br/>
        ![Alt text](src/main/resources/Outputs/6.png?raw=true "Optional Title") <br/>
