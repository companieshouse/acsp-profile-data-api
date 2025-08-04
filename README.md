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

## Terraform ECS

### What does this code do?

The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                | Description
:---------|:-----------------------------------------------------------------------------|:-----------
**ECS Cluster**        |public-data                                  | ECS cluster (stack) the service belongs to
**Load balancer**      |{env}-chs-apichgovuk / {env}-chs-apichgovuk-private   | The load balancer that sits in front of the service
**Concourse pipeline**     |[Pipeline link](https://ci-platform.companieshouse.gov.uk/teams/team-development/pipelines/acsp-profile-data-api) <br> [Pipeline code](https://github.com/companieshouse/ci-pipelines/blob/master/pipelines/ssplatform/team-development/acsp-profile-data-api)                                  | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)
