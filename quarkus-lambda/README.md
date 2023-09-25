# A simple servlet application with Quarkus in AWS Lambda

## Build

```
mvn clean package -DskipTests
```

## Prepare AWS environment

Install AWS-CLI and CDK, authenticate using AWS CLI

Then run

* `cd quarkus-function-infra`
* `cdk bootstrap` (installs CDK services to the AWS account, only once for an AWS account)
* `cdk deploy`

To update a function, run:

* `cdk deploy --hotswap`

Both options should reset warm lambda's even if the code doesn't change.

## Run

* `cd quarkus-function-infra`
* `./manage.sh invoke`

## Run as native GraalVM binary

### Build

```
mvn package -DskipTests -Pnative
```

### Prepare AWS environment

Run

* `cd quarkus-function-infra-native`
* `cdk deploy`

To update a function, run:

* `cdk deploy --hotswap`

Both options should reset warm lambda's even if the code doesn't change.

## Run

* `cd quarkus-function-infra-native`
* `./manage.sh invoke`