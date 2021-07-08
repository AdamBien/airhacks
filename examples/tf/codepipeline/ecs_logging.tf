# logs.tf

resource "aws_cloudwatch_log_group" "log_group_cloud_watch" {
  name              = "/airhacks/codepipeline/${var.service_name}"
  retention_in_days = 7
}

resource "aws_cloudwatch_log_stream" "ecr_ecs_fargate_stream" {
  name           = var.service_name
  log_group_name = aws_cloudwatch_log_group.log_group_cloud_watch.name
}