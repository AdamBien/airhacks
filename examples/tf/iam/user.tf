resource "aws_iam_user" "airhacks" {
  name = var.user_name

}

resource "aws_iam_access_key" "airhacks" {
  user = aws_iam_user.airhacks.name
}


resource "aws_iam_user_group_membership" "airhacks" {
  user = aws_iam_user.airhacks.name

  groups = [
    aws_iam_group.airhacks.name
  ]
}

