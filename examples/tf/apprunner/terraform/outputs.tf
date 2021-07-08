output "ecr_repository_url" {
  value = aws_ecr_repository.airhacks.repository_url
}

output "service_url" {
  value = "https://${aws_apprunner_service.airhacks.service_url}"
}