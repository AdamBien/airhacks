variable "aws_profile" {
  description = "code -n ~/.aws/credentials"
  type = string
  default = "cli"

}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "vpc_id" {
    type = string
    default = "vpc-4730572d"
}




