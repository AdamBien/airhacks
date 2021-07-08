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
}


resource "aws_s3_bucket" "airhacks" {
  bucket = var.bucket_name
  acl    = "public-read"

  tags = {
    Name        = "airhacks-test-bucket"
  }
}

output "bucket_domain_name" {
  value = aws_s3_bucket.airhacks.bucket_domain_name
}