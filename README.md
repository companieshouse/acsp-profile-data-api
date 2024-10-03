# `acsp-profile-data-api`

## Summary

The `acsp-profile-data-api` is a service that receives acsp profile deltas from
`acsp-profile-data-consumer`. It transforms these deltas to a standardised structure and then:

* stores or deletes documents within the `acsp_profile collection` in MongoDB, and
* enqueues a resource changed message that triggers further downstream processing.

The service is implemented in Java 21 using Spring Boot 3.2

## System requirements

* [Git](https://git-scm.com/downloads)
* [Java](http://www.oracle.com/technetwork/java/javase/downloads)
* [Maven](https://maven.apache.org/download.cgi)
* [MongoDB](https://www.mongodb.com/)

## Building and Running Locally using Docker

1. Clone [Docker CHS Development](https://github.com/companieshouse/docker-chs-development) and
   follow the steps in the
   README.
2. Enable the following services using the command `./bin/chs-dev services enable <service>`.
    * `acsp-profile-data-api`

3. Boot up the services' containers on docker using tilt `tilt up`.

## Environment variables

| Variable                           | Description                                                                           | Example (from docker-chs-development) |
|------------------------------------|---------------------------------------------------------------------------------------|---------------------------------------|
| PORT                               | The port at which the service is hosted in ECS                                        | 8080                                  |
| LOGLEVEL                           | The level of log messages output to the logs                                          | debug                                 |
| HUMAN_LOG                          | A boolean value to enable more readable log messages                                  | 1                                     |

## Building the docker image

```bash
mvn compile jib:dockerBuild
```

## To make local changes

Development mode is available for this service
in [Docker CHS Development](https://github.com/companieshouse/docker-chs-development).

```bash
./bin/chs-dev development enable acsp-profile-data-api
```

This will clone the `acsp-profile-data-api` into the `./repositories` folder. Any changes to the
code, or resources will automatically trigger a rebuild and relaunch.
