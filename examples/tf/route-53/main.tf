/*
Requires: 
AmazonRoute53AutoNamingFullAccess
AmazonRoute53FullAccess
*/
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
    }
  }
}


resource "aws_route53_zone" "airhacks" {
  name = "airhacks.private"

  vpc {
    vpc_id = var.aws_vpc
  }
 
}

resource "aws_route53_record" "airhacks" {
  zone_id = aws_route53_zone.airhacks.zone_id
  name    = "workshops.airhacks.private"
  type    = "A"
  ttl     = "300"
  records = ["192.42.42.42"]
}

output "zone_id"{
  value = aws_route53_zone.airhacks.zone_id
}

output "name_servers"{
  value = aws_route53_zone.airhacks.name_servers
}

output "tags_all"{
  value = aws_route53_zone.airhacks.tags_all
}