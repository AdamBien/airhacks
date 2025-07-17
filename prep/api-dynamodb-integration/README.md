# API Gateway DynamoDB Direct Integration

Serverless REST API with direct API Gateway (REST API) to DynamoDB integration (no custom code).

## API Endpoints

- `GET /registrations` - List all
- `GET /registrations/{id}` - Get by ID
- `POST /registrations` - Create new
- `DELETE /registrations/{id}` - Delete

## Usage

```bash
# Create registration
curl -X POST https://[api-id].execute-api.[region].amazonaws.com/registrations \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'

# Get by ID
curl https://[api-id].execute-api.[region].amazonaws.com/registrations/123

# Delete
curl -X DELETE https://[api-id].execute-api.[region].amazonaws.com/registrations/123
```

