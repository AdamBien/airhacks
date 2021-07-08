# ECR connect

apprunner is not available in Frankfurt

aws ecr get-login-password --region eu-west-1 --profile terraform  | docker login --username AWS --password-stdin 395440747665.dkr.ecr.eu-west-1.amazonaws.com

aws apprunner delete-service --service-arn arn:aws:apprunner:eu-west-1:395440747665:service/airhacks-apprunner-service/b6b1ecd757ff4144a9c6fe43f962f030 --profile terraform --region eu-west-1