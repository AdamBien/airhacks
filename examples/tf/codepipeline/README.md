# Pending github connections
https://eu-central-1.console.aws.amazon.com/codesuite/settings/connections?region=eu-central-1

# image detail

https://docs.aws.amazon.com/codepipeline/latest/userguide/file-reference.html#file-reference-ecs-bluegreen

# desired count

https://github.com/hashicorp/terraform-provider-aws/issues/13658

# pipeline CLI

aws codepipeline start-pipeline-execution --name hello-ecs-fargate-pipeline --profile terraform
aws codepipeline list-pipeline-executions --pipeline-name hello-ecs-fargate-pipeline --profile terraform | jq