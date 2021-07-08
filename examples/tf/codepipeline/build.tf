resource "aws_codebuild_project" "airhacks" {
  name          = "${var.app_name}-build"
  description   = "test_codebuild_project"
  build_timeout = "5"
  service_role  = aws_iam_role.codebuild.arn

  artifacts {
    //CODEPIPELINE, NO_ARTIFACTS, S3
    type = "NO_ARTIFACTS"
    name =""
  }

  cache {
    //https://docs.aws.amazon.com/codebuild/latest/userguide/build-caching.html
    //NO_CACHE, LOCAL, S3
    type     = "NO_CACHE"
  }

  source {
    //https://docs.aws.amazon.com/codebuild/latest/APIReference/API_ProjectSource.html
    //NO_CACHE, LOCAL, S3
    type     = "S3"
    location = "${aws_s3_bucket.codepipeline.bucket}/source"
  }

  environment {
    //https://docs.aws.amazon.com/codebuild/latest/userguide/build-env-ref-compute-types.html
    compute_type                = "BUILD_GENERAL1_MEDIUM" //"BUILD_GENERAL1_SMALL"
    //https://docs.aws.amazon.com/codebuild/latest/userguide/build-env-ref-available.html
    image                       = "aws/codebuild/standard:5.0"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"

    environment_variable {
      name  = "MESSAGE"
      value = "hello, CodeBuild"
      //PARAMETER_STORE, PLAINTEXT, SECRETS_MANAGER
      type  = "PLAINTEXT"
    }
  
    environment_variable{
       name = "AWS_DEFAULT_REGION"
       value = "eu-central-1"
    }
   
    environment_variable{
       name = "AWS_ACCOUNT_ID"
       value = var.aws_account_id
    }

    environment_variable {
      name = "AWS_ECS_CLUSTER_NAME"
      value = var.ecs_cluster_name
    }

    environment_variable {
      name = "AWS_ECS_SERVICE_NAME"
      value = var.service_name
    }

    environment_variable {
      name = "AWS_ECR_GROUP_NAME"
      value = var.ecr_group_name
    }
  
  }

  logs_config {
    cloudwatch_logs {
      group_name  = "/airhacks/codepipeline/"
      stream_name = "codebuild"
    }
  }



  vpc_config {
    vpc_id = aws_vpc.airhacks.id
    subnets = aws_subnet.private.*.id

    security_group_ids = [
      aws_security_group.airhacks.id
    ]
  }
  

  tags = {
    Environment = "Test"
  }
}