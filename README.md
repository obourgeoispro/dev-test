# dev-test

##How to build the project:

No need to install Gradle, the wrapper will download the correct distribution:

```
./gradlew bootRepackage
```

The bootRepackage instructs Spring Boot to build an executable fat jar.

## How to run the project:

Run the jar from the command line, for instance to get suggestions for Berlin:

```
java -jar .\build\libs\demo-0.0.1-SNAPSHOT.jar Berlin
``` 
