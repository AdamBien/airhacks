resource "aws_iam_role" "codepipeline" {
  name = "codepipeline"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [
            "codepipeline.amazonaws.com"
        ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}


resource "aws_iam_role_policy" "codepipeline" {
  name = "codepipeline"
  role = aws_iam_role.codepipeline.id

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid":"CodeBuildAccess",
      "Effect": "Allow",
      "Action": [
        "codebuild:BatchGetBuilds",
        "codebuild:StartBuild"
      ],
      "Resource": "*"
    },
     {
      "Sid":"S3PipelineAccess",
      "Effect": "Allow",
      "Action": [
        "s3:*"
      ],
      "Resource": "*"
      
    },
    {
        "Sid": "RepositoryConnectionsAccess",
        "Effect": "Allow",
        "Action": [
            "codestar-connections:CreateConnection",
            "codestar-connections:DeleteConnection",
            "codestar-connections:UseConnection",
            "codestar-connections:GetConnection",
            "codestar-connections:ListConnections",
            "codestar-connections:TagResource",
            "codestar-connections:ListTagsForResource",
            "codestar-connections:UntagResource"
        ],
        "Resource": "*"
     },
     {
        "Sid": "CodeDeployPipelineAccess",
        "Effect": "Allow",
        "Action": [
        "codedeploy:*"
        ],
        "Resource": "*"
     },
     {
        "Sid": "ECSPipelineAccess",
        "Effect": "Allow",
        "Action": [
        "ecs:*"
        ],
        "Resource": "*"
     },
    {
        "Sid": "EC2PipelineAccess",
        "Effect": "Allow",
        "Action": [
        "ec2:*"
        ],
        "Resource": "*"
     }, 
     {
      "Action": [  
          "codecommit:GetBranch",
          "codecommit:GetCommit",
          "codecommit:UploadArchive",
          "codecommit:GetUploadArchiveStatus",      
          "codecommit:CancelUploadArchive"
                ],
      "Resource": "*",
      "Effect": "Allow"
    },    
      {
            "Action": [
                "iam:PassRole"
            ],
            "Resource": "*",
            "Effect": "Allow",
            "Condition": {
                "StringEqualsIfExists": {
                    "iam:PassedToService": [
                        "ec2.amazonaws.com",
                        "ecs-tasks.amazonaws.com"
                    ]
                }
            }
        }
  ]
}
EOF
}