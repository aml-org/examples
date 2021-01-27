[![tested with jest](https://img.shields.io/badge/tested_with-jest-99424f.svg)](https://github.com/facebook/jest)

# AMF Examples
The purpose of this repository is to provide a set of usage examples for AMF and AML functions.
The examples are incremental, from simple to more complex.


## Usage

### Java
To run the Java examples you must build the project with gradle and then run the unit tests:
```bash
gradle clean build
gradle test
```

You can also run a specific file. For example, you can run only the parsing examples:
```bash
gradle test --tests "ParsingTest"
```

### JavaScript
To run the Javascript you need to build the project with npm and run the unit tests with jest:
```bash
npm install
jest
```
You can also run a specific file. For example, you can run only the parsing examples: 
```bash
jest parsing.test.js
```
