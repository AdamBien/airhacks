# API Gateway to Step Functions Integration

Direct AWS service integration between API Gateway REST API and Step Functions Express workflow using AWS CDK.

## Features

- REST API endpoint triggering Step Functions synchronously
- JSONata query language for data transformations
- Express workflow for synchronous execution
- BCE package structure
- CloudWatch access logging

## API

`POST /workflow` - Triggers the Step Functions workflow with request body as input

## Deploy

```bash
mvn package
cdk deploy
```

## Test

```bash
curl -X POST https://{api-id}.execute-api.eu-central-1.amazonaws.com/{stage}/workflow \
  -H "Content-Type: application/json" \
  -d '{"name": "Duke", "language": "Java", "age": 30}'
```

## References

- [JSONata Language Specification](https://docs.jsonata.org/overview)
- [Step Functions JSONata Support](https://docs.aws.amazon.com/step-functions/latest/dg/transforming-data-with-jsonata.html)
- [API Gateway Direct Service Integration](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-api-integration-types.html)
- [AWS CDK Step Functions](https://docs.aws.amazon.com/cdk/api/v2/docs/aws-cdk-lib.aws_stepfunctions-readme.html)
