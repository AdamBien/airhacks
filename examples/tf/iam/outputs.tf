output "user_arn"{
    value = aws_iam_user.airhacks.arn
}


output "aws_access_key_id"{
    value = aws_iam_access_key.airhacks.id
}

output "aws_secret_access_key"{
    value = aws_iam_access_key.airhacks.secret
}

output "airhacks-admins-arn"{
    value = aws_iam_group.airhacks.arn
}


