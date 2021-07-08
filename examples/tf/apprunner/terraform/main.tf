//https://docs.aws.amazon.com/apprunner/latest/api/API_CreateService.html

resource "aws_apprunner_service" "airhacks" {
  service_name = var.app_name
   tags = {
    Name = "${var.app_name}-service"
  }

  source_configuration {
    authentication_configuration {
      access_role_arn = aws_iam_role.access.arn
    }
    image_repository {
      image_configuration {
        port = "8080"
        runtime_environment_variables = tomap({
                message = "hello, apprunner"
            })
      }
      image_identifier      = "${aws_ecr_repository.airhacks.repository_url}:latest"
      image_repository_type = "ECR"
    }
  }
  instance_configuration {
    instance_role_arn =aws_iam_role.instance.arn
    cpu = 1024
    memory = 4096
  }
}