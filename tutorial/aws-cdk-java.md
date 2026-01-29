# AWS CDK with Java -- From REST to Infrastructure as Code

## What Is AWS CDK?

AWS CDK (Cloud Development Kit) lets you define cloud infrastructure using Java. You write Java code, CDK synthesizes it into CloudFormation templates, and deploys it to AWS.

No YAML. No JSON. Just Java.

## Prerequisites

- JDK 25+, Maven
- AWS CLI configured (`aws configure`)
- CDK CLI: `npm install -g aws-cdk`

## Core Concepts

CDK has three building blocks:

| Concept | Role | Analogy |
|---|---|---|
| **App** | Entry point | `main()` method |
| **Stack** | Deployable unit | A WAR/JAR file |
| **Construct** | A single AWS resource | A class instance |

They form a tree:

```
App
 └── Stack
      ├── Lambda Function
      ├── API Gateway
      └── DynamoDB Table
```

## REST on AWS

A REST API you'd build with JAX-RS maps to AWS like this:

```
JAX-RS Container  →  API Gateway     (HTTP routing)
Resource class    →  Lambda Function  (business logic)
JPA / Database    →  DynamoDB         (persistence)
```

No application server. Each HTTP request triggers a Lambda function.

---

## 1. Bootstrapping a CDK Project

```bash
mkdir greetings-cdk && cd greetings-cdk
cdk init app --language java
```

This generates a Maven project with:

- `src/main/java/.../GreetingsCdkApp.java` -- the entry point
- `src/main/java/.../GreetingsCdkStack.java` -- the infrastructure definition
- `pom.xml` -- with CDK dependencies
- `cdk.json` -- CDK configuration

## 2. The Entry Point

```java
public class GreetingsCdkApp {
    public static void main(String[] args) {
        var app = new App();
        new GreetingsStack(app, "GreetingsStack");
        app.synth();
    }
}
```

`app.synth()` converts your Java objects into a CloudFormation template. Nothing is deployed yet.

## 3. Your First Stack -- An S3 Bucket

The simplest possible infrastructure:

```java
public class GreetingsStack extends Stack {

    public GreetingsStack(Construct scope, String id) {
        super(scope, id);

        Bucket.Builder.create(this, "GreetingsBucket")
                .versioned(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }
}
```

Key points:
- `this` = the scope (parent in the construct tree)
- `"GreetingsBucket"` = logical ID (unique within the stack)
- `Builder.create(scope, id)` = preferred over `new Bucket(...)` constructors

Deploy it:

```bash
cdk deploy
```

Tear it down:

```bash
cdk destroy
```

## 4. Adding a Lambda Function

A Lambda function is Java code packaged as a JAR. Create a separate Maven module or project for the function code.

### The Lambda Handler

```java
public class GreetingsHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        return Map.of(
            "statusCode", 200,
            "body", "{\"message\": \"hello from lambda\"}"
        );
    }
}
```

### The CDK Stack with Lambda

```java
public class GreetingsStack extends Stack {

    public GreetingsStack(Construct scope, String id) {
        super(scope, id);

        Function.Builder.create(this, "GreetingsFunction")
                .runtime(Runtime.JAVA_21)
                .handler("airhacks.greetings.boundary.GreetingsHandler::handleRequest")
                .code(Code.fromAsset("../lambda/target/lambda.jar"))
                .memorySize(512)
                .timeout(Duration.seconds(30))
                .build();
    }
}
```

- `handler` = fully qualified class name + method
- `code` = path to the built JAR
- `memorySize` = also determines CPU allocation

## 5. Exposing the Lambda via API Gateway

This is where your REST endpoint comes to life:

```java
public class GreetingsStack extends Stack {

    public GreetingsStack(Construct scope, String id) {
        super(scope, id);

        var handler = Function.Builder.create(this, "GreetingsFunction")
                .runtime(Runtime.JAVA_21)
                .handler("airhacks.greetings.boundary.GreetingsHandler::handleRequest")
                .code(Code.fromAsset("../lambda/target/lambda.jar"))
                .memorySize(512)
                .timeout(Duration.seconds(30))
                .build();

        var api = LambdaRestApi.Builder.create(this, "GreetingsApi")
                .handler(handler)
                .build();
    }
}
```

`LambdaRestApi` is a high-level construct that:
- Creates an API Gateway REST API
- Routes **all** HTTP requests to the Lambda
- Configures the necessary IAM permissions automatically

After `cdk deploy`, you get an API URL like:

```
https://abc123.execute-api.eu-central-1.amazonaws.com/prod/
```

### Custom Routes

For more control over routing:

```java
var api = RestApi.Builder.create(this, "GreetingsApi")
        .restApiName("Greetings Service")
        .build();

var greetings = api.getRoot().addResource("greetings");
greetings.addMethod("GET", new LambdaIntegration(handler));
greetings.addMethod("POST", new LambdaIntegration(handler));

var single = greetings.addResource("{id}");
single.addMethod("GET", new LambdaIntegration(handler));
single.addMethod("DELETE", new LambdaIntegration(handler));
```

This creates:
- `GET /greetings`
- `POST /greetings`
- `GET /greetings/{id}`
- `DELETE /greetings/{id}`

## 6. Adding DynamoDB

```java
var table = Table.Builder.create(this, "GreetingsTable")
        .tableName("greetings")
        .partitionKey(Attribute.builder()
                .name("id")
                .type(AttributeType.STRING)
                .build())
        .removalPolicy(RemovalPolicy.DESTROY)
        .build();

// Grant the Lambda read/write access
table.grantReadWriteData(handler);
```

`table.grantReadWriteData(handler)` -- this is the CDK way. Instead of manually writing IAM policies, you use `grant*` methods. CDK generates the minimal IAM permissions for you.

Pass the table name to the Lambda via environment variables:

```java
var handler = Function.Builder.create(this, "GreetingsFunction")
        .runtime(Runtime.JAVA_21)
        .handler("airhacks.greetings.boundary.GreetingsHandler::handleRequest")
        .code(Code.fromAsset("../lambda/target/lambda.jar"))
        .memorySize(512)
        .timeout(Duration.seconds(30))
        .environment(Map.of("TABLE_NAME", table.getTableName()))
        .build();
```

## 7. The Complete Stack

```java
public class GreetingsStack extends Stack {

    public GreetingsStack(Construct scope, String id) {
        super(scope, id);

        var table = Table.Builder.create(this, "GreetingsTable")
                .tableName("greetings")
                .partitionKey(Attribute.builder()
                        .name("id")
                        .type(AttributeType.STRING)
                        .build())
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        var handler = Function.Builder.create(this, "GreetingsFunction")
                .runtime(Runtime.JAVA_21)
                .handler("airhacks.greetings.boundary.GreetingsHandler::handleRequest")
                .code(Code.fromAsset("../lambda/target/lambda.jar"))
                .memorySize(512)
                .timeout(Duration.seconds(30))
                .environment(Map.of("TABLE_NAME", table.getTableName()))
                .build();

        table.grantReadWriteData(handler);

        LambdaRestApi.Builder.create(this, "GreetingsApi")
                .handler(handler)
                .build();
    }
}
```

Three resources. Automatic IAM permissions. Deployed with a single command.

## 8. Useful CDK Commands

| Command | What it does |
|---|---|
| `cdk synth` | Generate CloudFormation template (no deploy) |
| `cdk diff` | Show what changed since last deploy |
| `cdk deploy` | Deploy the stack to AWS |
| `cdk destroy` | Remove all resources |
| `cdk ls` | List all stacks in the app |

## 9. Key Principles

1. **Use high-level constructs** -- `LambdaRestApi` over manually wiring API Gateway. Avoid `Cfn*` constructs.
2. **Use `grant*` methods** -- `table.grantReadWriteData(fn)` over manual IAM policy JSON.
3. **Use `Builder.create`** -- preferred over constructor invocation.
4. **One stack per deployable unit** -- keep stacks independent, never pass stacks to each other.
5. **`cdk diff` before `cdk deploy`** -- always review changes before deploying.

## 10. Maven Dependencies for CDK

```xml
<dependencies>
    <dependency>
        <groupId>software.amazon.awscdk</groupId>
        <artifactId>aws-cdk-lib</artifactId>
        <version>2.178.0</version>
    </dependency>
    <dependency>
        <groupId>software.constructs</groupId>
        <artifactId>constructs</artifactId>
        <version>10.4.2</version>
    </dependency>
</dependencies>
```

A single `aws-cdk-lib` dependency covers all AWS services. No need to add per-service dependencies.

## Next Steps

- Add a custom domain with `DomainName` and Route53
- Add authentication with Cognito (always in a dedicated stack)
- Add Step Functions for orchestration workflows
- Add CloudWatch alarms and dashboards
