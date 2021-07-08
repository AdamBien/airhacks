resource "aws_ecr_repository" "airhacks" {
  name                 = "airhacks/${var.app_name}"
  image_tag_mutability = "MUTABLE"
  image_scanning_configuration {
    scan_on_push = false
  }
}

