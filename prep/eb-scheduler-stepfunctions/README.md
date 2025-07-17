# EventBridge Scheduler with Step Functions

AWS CDK project that integrates EventBridge Scheduler with Step Functions for automated workflow orchestration.

## Architecture

- **EventBridge Scheduler**: Triggers workflows every 5 minutes
- **Step Functions**: Orchestrates workflow with Lambda integration
- **Lambda Function**: Processes scheduled tasks

## Build & Deploy

```bash
mvn clean package
cdk deploy
```

## Components

- `SchedulerStepFunctionsStack`: Main CDK stack
- `StateMachineCreator`: Creates Step Functions workflow
- `SchedulerCreator`: Configures EventBridge Scheduler

## Workflow

1. EventBridge Scheduler triggers every 5 minutes
2. Step Functions workflow starts
3. Lambda function processes the task
4. Workflow completes based on processing result
