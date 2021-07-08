variable "aws_profile" {
  type = string
  default = "cli"
}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "vpc_name" {
    type = string
    default = "ig-public-nat-private"
}

variable "cidr_block" {
    type = string
    default = "10.2.0.0/16"
}