resource "aws_codecommit_repository" "airhacks" {
  repository_name = "airhacks-code-repository"
  description     = "airhacks.live code repository"
  default_branch  = "main"
}

output "repository_id"{
    value = aws_codecommit_repository.airhacks.repository_id
}


output "arn"{
    value = aws_codecommit_repository.airhacks.arn
}

output "clone_url_http"{
    value = aws_codecommit_repository.airhacks.clone_url_http
}

output "clone_url_ssh"{
    value = aws_codecommit_repository.airhacks.clone_url_ssh
}