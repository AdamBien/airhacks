# API Gateway to EventBridge Direct Integration

Serverless architecture demonstrating direct integration between API Gateway and EventBridge without Lambda functions.

## Architecture

- **API Gateway**: REST API accepting PUT requests at `/events`
- **EventBridge**: Custom event bus processing API requests as events
- **CloudWatch Logs**: Captures all events for monitoring

## Request Flow

1. PUT request to `/events` endpoint
2. API Gateway transforms payload to EventBridge event format
3. EventBridge publishes event with source `api.gateway`
4. CloudWatch Logs captures event for inspection

## Deployment

```bash
mvn package
cdk deploy
```

## Testing

```bash
curl -X PUT https://[api-id].execute-api.eu-central-1.amazonaws.com/prod/events \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello EventBridge"}'
```

Check CloudWatch Logs group `/aws/events/airhacks-api-gateway-events` for captured events.

See you at: [airhacks.live](https://airhacks.live)
