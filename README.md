# `acsp-profile-data-api`

The `acsp-profile-data-api` is a service that receives acsp profile deltas from
`acsp-profile-data-consumer`. It transforms these deltas to a standardised structure and then stores them as documents
within the `acsp_profile` collection in MongoDB. The documents are then readily available to be retrieved by either of
the services' GET endpoints described [below](#Endpoints).

The service is implemented in Java 21 using Spring Boot 3.2

## System requirements

* [Git](https://git-scm.com/downloads)
* [Java](http://www.oracle.com/technetwork/java/javase/downloads)
* [Maven](https://maven.apache.org/download.cgi)
* [MongoDB](https://www.mongodb.com/)

## Getting started

### Building and running locally using docker

1. Clone [Docker CHS Development](https://github.com/companieshouse/docker-chs-development) and follow the steps in the
   README.
2. Enable the required services by running the following command, in the `docker-chs-development` directory:
   ```
   chs-dev services enable chs-delta-api \
   acsp-profile-delta-consumer \
   acsp-profile-data-api \
   authentication-service \ 
   ch-gov-uk
   ```
3. Boot up the services' containers on docker using `chs-dev up`.
4. Messages can be produced to the acsp-profile-delta topic using the instructions given
   in [CHS Delta API](https://github.com/companieshouse/chs-delta-api).

### Building the docker image with local changes, requires access to AWS ECR

```bash
  mvn compile jib:dockerBuild
```

### Environment variables

| Variable  | Description                                          | Example (from docker-chs-development) |
|-----------|------------------------------------------------------|---------------------------------------|
| PORT      | The port at which the service is hosted in ECS       | 8080                                  |
| LOGLEVEL  | The level of log messages output to the logs         | debug                                 |
| HUMAN_LOG | A boolean value to enable more readable log messages | 1                                     |

## Other useful information

### Endpoints

The table below describes the endpoints the service has available

| Method | URI                                                                  | Description                                                                                                                  |
|--------|----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| GET    | `/authorised-corporate-service-providers/{acsp_number}`              | Returns a partial representation of an ACSP profile (requires internal app privileges).                                      |
| GET    | `/authorised-corporate-service-providers/{acsp_number}/full-profile` | Returns a full ACSP profile including sensitive data (requires sensitive data privileges).                                   |
| PUT    | `/authorised-corporate-service-providers/{acsp_number}/internal`     | Inserts or updates an existing ACSP profile within the collection, includes checks for delta staleness and Mongo versioning. |

