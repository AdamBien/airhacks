//https://docs.aws.amazon.com/codepipeline/latest/userguide/tutorials-ecs-ecr-codedeploy.html
resource "aws_codestarconnections_connection" "airhacks" {
  name          = "github-connection"
  provider_type = "GitHub"
}

resource "aws_codepipeline" "airhacks" {
  name     = "${var.app_name}-pipeline"
  role_arn = aws_iam_role.codepipeline.arn
  
  artifact_store {
    location = aws_s3_bucket.codepipeline.bucket
    type     = "S3"
  }  
/* github.com configuration
  stage {
      name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeStarSourceConnection"
      version          = "1"
      output_artifacts = ["source"]

      configuration = {
        ConnectionArn    = aws_codestarconnections_connection.airhacks.arn
        FullRepositoryId = "adambien/awscodebuild"
        BranchName       = "main"
      }
    }
  }
*/

  stage {
      name = "Source"
    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeCommit"
      version          = "1"
      output_artifacts = ["source"]

      configuration = {
        RepositoryName = var.codecommit_repository_name
        BranchName       = "main"
      }
    }
  }


//https://docs.aws.amazon.com/codepipeline/latest/userguide/action-reference-CodeBuild.html
  stage {
    name = "Build"

    action {
      name             = "Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      input_artifacts  = ["source"]
      output_artifacts = ["appspec","taskdef","st"]
      version          = "1"

      configuration = {
        ProjectName = aws_codebuild_project.airhacks.name
      }
    }
  }

//https://docs.aws.amazon.com/codepipeline/latest/userguide/action-reference-CodeDeploy.html
  stage {
    name = "Deploy"

    action {
      name            = "Deploy"
      category        = "Deploy"
      owner           = "AWS"
      provider        = "CodeDeployToECS"
      //access to github needed
      //https://docs.aws.amazon.com/codepipeline/latest/userguide/tutorials-ecs-ecr-codedeploy.html#tutorials-ecs-ecr-codedeploy-pipeline
      //https://docs.aws.amazon.com/codedeploy/latest/userguide/reference-appspec-file.html#appspec-reference-ecs
      input_artifacts = ["appspec","taskdef"]
      version         = "1"

      //https://docs.aws.amazon.com/codepipeline/latest/userguide/action-reference-ECSbluegreen.html
      configuration = {
        ApplicationName     = var.app_name
        DeploymentGroupName   = aws_codedeploy_deployment_group.airhacks.deployment_group_name
        AppSpecTemplateArtifact = "appspec"
        TaskDefinitionTemplateArtifact = "taskdef"
        TaskDefinitionTemplatePath = "taskdef.json"
        AppSpecTemplatePath = "appspec.yml"
        //Item 6. => https://docs.aws.amazon.com/codepipeline/latest/userguide/tutorials-ecs-ecr-codedeploy.html
        Image1ArtifactName = "taskdef"
        Image1ContainerName = "IMAGE1_NAME"
      }
    }
  }
  stage {
    name = "SystemTest"
    action {
          run_order = 0
          name             = "SystemTest"
          category         = "Test"
          owner            = "AWS"
          provider         = "CodeBuild"
          input_artifacts  = ["st"]
          version          = "1"
          configuration = {
            ProjectName = var.app_name
            EnvironmentVariables = jsonencode([
            {
              name  = "HELLO_RESOURCE_MP_REST_URL"
              value = "http://${aws_alb.airhacks.dns_name}/"
              type  = "PLAINTEXT"
            }
          ])
          }
        }
      
      action {
          run_order = 1
          name             = "TortureTest"
          category         = "Test"
          owner            = "AWS"
          provider         = "CodeBuild"
          input_artifacts  = ["st"]
          version          = "1"
          configuration = {
            ProjectName = var.app_name
            EnvironmentVariables = jsonencode([
            {
              name  = "HELLO_RESOURCE_MP_REST_URL"
              value = "http://${aws_alb.airhacks.dns_name}/"
              type  = "PLAINTEXT"
            },
            {
              name  = "LAUNCH_TORTURE"
              value = "true"
              type  = "PLAINTEXT"
            }
          ])
          }
        }
      }      
}