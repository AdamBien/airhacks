terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
  profile = var.aws_profile
  default_tags{
    tags = {
      environment = "workshops"
      project     = "airhacks.live"
      application = "web-ssh-security-group"
      purpose     = "a reusable security group for EC2"
    }
  }
}
