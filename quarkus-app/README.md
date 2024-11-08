# A simple servlet application with Quarkus

## Build

```shell script
mvn clean package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

## Run

```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

Then access the app at http://localhost:8080/

## Run with timing

```
STARTUP_PHRASE='started in' bash ../measure-startup-time.sh \
  quarkus http://localhost:8080 java -jar target/quarkus-app/quarkus-run.jar
```

## Uber JAR

If you want to build an _über-jar_, execute the following command:
```shell script
mvn package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using:

```
java -jar target/*-runner.jar`.
```

## Creating a native executable

You can create a native executable using: 
```shell script
mvn package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
mvn package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

