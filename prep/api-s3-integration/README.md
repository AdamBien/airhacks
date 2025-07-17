# REST API Gateway to S3 Integration

A serverless AWS CDK application that creates a REST API Gateway with direct S3 integration for managing registrations.

## Architecture

This project demonstrates a serverless pattern where API Gateway directly integrates with S3 without Lambda functions:

- **REST API**: `/registrations/{id}` endpoints for GET and PUT operations
- **S3 Bucket**: Stores registration data with automatic region-based naming
- **IAM**: Uses CDK grant methods for secure S3 access

## API Endpoints

- `PUT /registrations/{id}` - Store registration data
- `GET /registrations/{id}` - Retrieve registration data

## Usage Examples

Store a registration:
```bash
curl -X PUT https://YOUR-API-ID.execute-api.REGION.amazonaws.com/prod/registrations/duke-24 \
  -H "Content-Type: application/json" \
  -d '{"name": "Duke", "email": "duke@sun.com"}'
```

Retrieve a registration:
```bash
curl https://YOUR-API-ID.execute-api.REGION.amazonaws.com/prod/registrations/duke-24
```

## Documentation

- [API Gateway AWS Service Integration](https://docs.aws.amazon.com/apigateway/latest/developerguide/getting-started-aws-proxy.html)
- [API Gateway S3 Integration](https://docs.aws.amazon.com/apigateway/latest/developerguide/integrating-api-with-aws-services-s3.html)
- [AWS CDK API Reference](https://docs.aws.amazon.com/cdk/api/v2/java/index.html)

See you at: [airhacks.live](https://airhacks.live)
