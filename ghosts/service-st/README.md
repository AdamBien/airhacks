# service-st

To perform black box tests locally (uses http://localhost:8080 by default):

```
mvn clean test-compile failsafe:integration-test
```

To test against a remote environment, set the BASE_URI environment variable:

```
export BASE_URI=https://deployed.com
mvn clean test-compile failsafe:integration-test
```