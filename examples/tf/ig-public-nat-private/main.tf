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
      project = "airhacks.live"
      application = var.vpc_name
    }
  }
}


resource "aws_vpc" "airhacks" {
  cidr_block       = var.cidr_block
  instance_tenancy = "default"
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    Name = var.vpc_name
  }
}
resource "aws_subnet" "public" {
  vpc_id     = aws_vpc.airhacks.id
  cidr_block = cidrsubnet(var.cidr_block,4,0)
  tags = {
    Name = "${var.vpc_name}-public-subnet"
  }

}

resource "aws_subnet" "private" {
  vpc_id     = aws_vpc.airhacks.id
  cidr_block = cidrsubnet(var.cidr_block,4,1)
  tags = {
    Name = "${var.vpc_name}-private-subnet"
  }
}

output "aws_vpc"{
  value = aws_vpc.airhacks.id
}

output "public_subnet"{
  value = aws_subnet.public.id
}

output "private_subnet"{
  value = aws_subnet.private.id
}


