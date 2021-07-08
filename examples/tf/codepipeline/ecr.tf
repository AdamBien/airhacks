resource "aws_ecr_repository" "docker_registry" {
  name                 = "${var.ecr_group_name}/${var.service_name}"
  image_tag_mutability = "MUTABLE"
  image_scanning_configuration {
    scan_on_push = false
  }
}