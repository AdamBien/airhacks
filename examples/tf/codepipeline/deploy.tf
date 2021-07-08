resource "aws_codedeploy_app" "airhacks" {
  compute_platform = "ECS"
  name             = var.app_name
}

resource "aws_codedeploy_deployment_group" "airhacks" {
  app_name               = aws_codedeploy_app.airhacks.name
  deployment_config_name = "CodeDeployDefault.ECSAllAtOnce"
  deployment_group_name  = var.deployment_group_name
  service_role_arn       = aws_iam_role.codedeploy.arn

  auto_rollback_configuration {
    enabled = true
    events  = ["DEPLOYMENT_FAILURE"]
  }

  deployment_style {
    deployment_option = "WITH_TRAFFIC_CONTROL"
    deployment_type   = "BLUE_GREEN"
  }

  ecs_service {
    cluster_name = aws_ecs_cluster.airhacks.name
    service_name = aws_ecs_service.airhacks.name
  }

  load_balancer_info {
    target_group_pair_info {
        prod_traffic_route {
          listener_arns = [aws_alb_listener.airhacks.arn]
        }

        target_group {
          name = var.aws_alb_target_group_blue_name
        }

        target_group {
          name = var.aws_alb_target_group_green_name
        }
      }  
  }
  
  blue_green_deployment_config {
    deployment_ready_option {
      action_on_timeout = "CONTINUE_DEPLOYMENT"
    }

    terminate_blue_instances_on_deployment_success {
      action                           = "TERMINATE"
      termination_wait_time_in_minutes = 5
    }
  }


  
}