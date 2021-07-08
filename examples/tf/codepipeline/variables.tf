
variable "aws_profile" {
  description = "code -n ~/.aws/credentials"
  type = string
  default = "cli"

}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "app_name"{
    type = string
}

variable "vpc_name" {
    type = string
    default = "codepipeline"
}

variable "cidr_block" {
    type = string
    default = "10.1.0.0/16"
}
variable "ecs_cluster_name"{
  type = string
}


variable "deployment_group_name"{
  type = string
  default = "airhacks"
}

variable "service_name"{
  type = string
  default = "quarkus-codepipeline-fargate"
}

variable "cluster_name" {
  type = string
  default = "quarkus-ecr-ecs-cluster"
}

variable "aws_alb_target_group_green_name"{
  type = string
}
variable "aws_alb_target_group_blue_name"{
  type = string
}

variable "s3_bucket_codepipeline_name"{
  type = string
  default = "airhacks-codepipeline"
}

variable "ecr_group_name" {
  type = string
  default = "airhacks"
}

variable "aws_ecs_service_desired_count"{
  type = string
  description = "due to a bug, desired_count cannot be changed by terraform, see: https://github.com/hashicorp/terraform-provider-aws/issues/13658"
  default = 0
}

variable "aws_account_id"{
  type = string
}

variable "codecommit_repository_name"{
  type = string
  default = "airhacks-code-repository"
}