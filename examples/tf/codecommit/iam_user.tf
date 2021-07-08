resource "aws_iam_user" "airhacks" {
  name = "airhacks-codecommit-developer"
  path = "/airhacks/"

}

resource "aws_iam_user_policy_attachment" "airhacks" {
  user       = aws_iam_user.airhacks.name
  policy_arn = "arn:aws:iam::aws:policy/AWSCodeCommitPowerUser"
}

