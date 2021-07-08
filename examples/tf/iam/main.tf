terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

locals {
  region = var.aws_region
}

provider "aws" {
  region = local.region
  profile = var.aws_profile
  default_tags{
    tags = {
      environment = "workshops"
      name = "airhacks.live"
      project = "iam"
    }
  }
}
