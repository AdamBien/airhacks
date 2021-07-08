variable "aws_profile" {
  description = "code -n ~/.aws/credentials"
  type = string
  default = "cli"
}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "subnet_id" {
    type = string
    default = "subnet-c8612ea2"
}

variable "aws_vpc" {
    type = string
    default = "vpc-4730572d"
}

variable "instance_type"{
    type = string
    default = "t4g.micro"
}