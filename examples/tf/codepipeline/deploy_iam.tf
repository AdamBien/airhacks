//https://docs.aws.amazon.com/AmazonECS/latest/userguide/codedeploy_IAM_role.html
resource "aws_iam_role" "codedeploy" {
  name = "codedeploy-role"

  assume_role_policy = <<ASSUME
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "codedeploy.amazonaws.com"
          ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
ASSUME
}


resource "aws_iam_policy" "passrole" {
  name        = "codedeploy-passrole"
  description = "policy suggested by: https://docs.aws.amazon.com/AmazonECS/latest/userguide/codedeploy_IAM_role.html"

  policy = <<POLICY
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "elasticloadbalancing:*",
            "Resource": [
                "*"
            ]
        }
    ]
}
POLICY
}

//...does not give you permission to perform operations in the following AWS service: AmazonElasticLoadBalancingV2
resource "aws_iam_policy" "elbv2" {
  name        = "codedeploy-elbv2"
  description = "policy suggested by: https://docs.aws.amazon.com/AmazonECS/latest/userguide/codedeploy_IAM_role.html"

  policy = <<POLICY
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "elasticloadbalancing:*",
            "Resource": [
                "*"
            ]
        }
    ]
}
POLICY
}

//https://docs.aws.amazon.com/AmazonECS/latest/userguide/deployment-type-bluegreen.html#deployment-type-bluegreen-IAM
resource "aws_iam_role_policy_attachment" "codedeploy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonECS_FullAccess"
  role       = aws_iam_role.codedeploy.name
}


resource "aws_iam_role_policy_attachment" "codedeploy-passrole" {
  policy_arn = aws_iam_policy.elbv2.arn
  role       = aws_iam_role.codedeploy.name
}

//https://docs.aws.amazon.com/service-authorization/latest/reference/list_elasticloadbalancingv2.html
resource "aws_iam_role_policy_attachment" "codedeploy-elbv2" {
  policy_arn = aws_iam_policy.passrole.arn
  role       = aws_iam_role.codedeploy.name
}