variable "aws_profile" {
  type = string
  default = "cli"
}

variable "aws_region" {
    type = string
    default = "eu-central-1"
}

variable "bucket_name" {
    type = string
    default = "airhacks-test-bucket"
}