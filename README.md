[![tested with jest](https://img.shields.io/badge/tested_with-jest-99424f.svg)](https://github.com/facebook/jest)

# AMF Examples
The purpose of this repository is to provide a set of usage examples for AMF and AML functions.
The examples are incremental, from simple to more complex.


# AMF 4 examples

### Java
To run the Java examples you must build the project with gradle and then run the unit tests:
```bash
gradle :AMF4:clean build
gradle :AMF4:test
```

You can also run a specific file. For example, you can run only the parsing examples:
```bash
gradle :AMF4:test --tests "ParsingTest"
```

### JavaScript
To run the Javascript you need to go to the AMF4 folder, build the project with npm and run the unit tests with jest,
it can all be done running the following command on the root folder:
```bash
npm run test-amf4
```
You can also run a specific file. For example, you can run only the parsing examples: 
```bash
jest parsing.test.js
```

# AMF 5 examples

### Java
To run the Java examples you must build the project with gradle and then run the unit tests:
```bash
gradle :AMF4:clean build
gradle :AMF5:test
```

### Scala
To run the Scala examples you must build the project with gradle and then run the scalatest unit tests:
```bash
gradle :AMF5:clean build
gradle :AMF5:scalatest
```

### TypeScript
To run the Javascript you need to go to the AMF5 folder, build the project with npm and run the unit tests,
it can all be done running the following command on the root folder:
```bash
npm run test-amf5
```

### All tests
You can run all AMF 5 tests (Scala, Java and TypeScript) by running the following task:
```bash
gradle :AMF5:testAll
```
