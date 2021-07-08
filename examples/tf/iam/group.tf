resource "aws_iam_policy" "airhacks" {
    name = "${aws_iam_group.airhacks.name}-policy"
    policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
       {  
            "Effect": "Allow",
            "Action": [
                "*"
            ],
            "Resource": "*"
        }        
    ]
}
EOF

}

resource "aws_iam_group" "airhacks" {
    name = var.aws_iam_group_name
}

resource "aws_iam_group_membership" "airhacks"{
    name = "${aws_iam_group.airhacks.name}-membership"
    group = aws_iam_group.airhacks.name
    users = [var.user_name]
}

resource "aws_iam_group_policy_attachment" "airhacks" {
    group = aws_iam_group.airhacks.name
    policy_arn = aws_iam_policy.airhacks.arn
}


