[![CircleCI](https://circleci.com/gh/lorenzomartino86/money-transfer-app.svg?style=svg)](https://circleci.com/gh/lorenzomartino86/money-transfer-app)
[![codecov](https://codecov.io/gh/lorenzomartino86/money-transfer-app/branch/master/graph/badge.svg)](https://codecov.io/gh/lorenzomartino86/money-transfer-app)
# Money Transfer App
A Java RESTful API to handle money transfers between different accounts.

### Architecture
This application has been splitted in following maven modules:

1. **money-transfer-domain:** Layer responsible of the business domain.
2. **money-transfer-repository:** Layer responsible of persist the relevant data.
3. **money-transfer-rest-api:** Layer responsible of exposing a lightweight Rest API.
4. **money-transfer-app:** Main partition and entrypoint to boot the application.

I was inspired by the Hexagonal architecture in order to build software components high cohesive and loosely coupled. 
The following diagram resumes the dependencies between the various layers and as you can see the domain layer doesn't depend on any other layer.
![alt text](money-transfer-domain/design/high-level-arch.png)

### Technical Background

### Build Instructions
```sh
mvn clean install
```

### Run Instructions
After build you can run the standalone executable jar located at *money-transfer-app/target*
```sh
java -jar money-transfer-app/target/money-transfer-app-1.0.0-SNAPSHOT.jar
```

### Demo


### Documentations

#### RestFul API Design
See the [api documentation](money-transfer-rest-api/README.md)

#### Domain Design
See the [domain documentation](money-transfer-domain/README.md)

#### Repository
See the [repository documentation](money-transfer-repository/README.md)

#### Rest Api
See the [rest api documentation](money-transfer-rest-api/README.md)

### Resources
- [SparkJava](http://sparkjava.com/)
- [ormlite](http://ormlite.com/)
- [Guice](https://github.com/google/guice)
- [Hexagonal Architecture](https://dzone.com/articles/writing-a-microservice-in-golang-which-communicate)