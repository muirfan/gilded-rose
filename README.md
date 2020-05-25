# Gilded Rose API Service
## Introduction
It is a HTTP RESTful API developed for Gilded Rose, a small inn in a prominent city that buys and sells only the finest items. This API allows merchants in other cities to access the shop's inventory as well as order items while quantities last. It also supports dynamic pricing based on surge pricing model.

## Assumptions and limitations
The solution is designed and developed with following assumptions and limitations:
- We are not using a DB, hence entities are persisted in memory only. Likewise, the data would be reset on service shutdown/restart. Similarly DTO objects are not created as those are driven by DB entitites.
- A simple fixed API key based authentication method is implemented as POC
- Unit tests are implemented in each layer (Controller, Service, Repository) to demonstrate the approach and pattern. Further work is required to achieve 90% plus coverage for a production-ready solution, however it would be just following the same pattern.
- Integration tests are written in Postman
- Price was marked as int in specs but during testing it turned out that it lost the factional vlaue while computing surge price hence chaged it to double. Also, added rounding to two decimal places.

## Architecture and design
This service is built upon conventional Java (v9) Spring Boot and Spring Web/REST framework. Here are the key points of design. 

### Persistance layer
Generally it is implemented at DB and then we create repositories in Java application to interact with DB. But for the sake of simplicity and as per requirements the solution is designed without use of a DB. Instead, the repositories use in-memory object collections to mimic DB.

### Surge Pricing
In order to address these requirements, SurgeMonitorService class has been developed which monitors items views/listings. Likewise, when the calls in last one hour exceeds configured threshold (10 right now) it enables price increase by configurable percentage (currently set to 10%).
Serious considerations are put in to making the design efficient and perform under high load by using Sliding Window concept along with dividing the sliding window in to slots for performance consistency at higher loads. A slot is basically a subdivision of rolling window to help aggregate the stats within the slot instead of recording and aggregating indidual view/list calls. I.e., with current slot size of 1 minute, there would be only 60 counters maintained at any given time independent of the load.
Currently this logic is confined within each server and limits horizontal scalability, a shared cache (e.g., RedisCache) can further improve this in future.

### Security
The secured transaction i.e., purchase an item by submitting purchase order is protected with well-know API Key
based API authentication. An API Key represent a particular user's credentials and likewise can be traced back to the user's profile.
For the sake of simplicity and as a proof of concept the API Key is hard-coded in the service but for a production-ready
design it require complete module to handle user profiles and keys or we may use a managed cloud service e.g., Azure API Management service that provides turn key solution. Another future consideration is OAuth based authentication that provides greater security and reliablity.

### Published Endpoints
It exposes following endpoints.

GET /items
It returns list of items available for purchase.

POST /orders
Submit a purchase order.

Errors:
- 401: Invalid API Key
- 404: Invalid/unknown/unavailable item

## How to build
```bash
mvn clean install
```

## Run Unit Tests
```bash
mvn clean test
```

## Run Local Instance
```bash
mvn spring-boot:run
```
Local Base URL: http://localhost:8080/

## Run Postman Integration Tests
- Start the local instance as above.
- Run Postman and load "integration-tests/Gilded-Rose.postman_collection.json"
- Execute the tests
