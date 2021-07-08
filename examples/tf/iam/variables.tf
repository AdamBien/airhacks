variable "aws_profile" {
  description = "code -n ~/.aws/credentials"
  type = string
  default = "cli"
}

variable "account_id" {
    type = string
}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "aws_iam_group_name" {
    type = string
    default = "tf-airhacks-admin"
}


variable "user_name"{
    type = string
    default = "tf-cli"
}




