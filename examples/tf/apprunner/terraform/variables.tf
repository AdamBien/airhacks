variable "aws_profile" {
  description = "code -n ~/.aws/credentials"
  type = string
  default = "cli"

}

variable "aws_region" {
    type = string
    default = "eu-west-1"
}

variable "app_name" {
    type  = string
    default = "airhacks-apprunner-service" 
}



