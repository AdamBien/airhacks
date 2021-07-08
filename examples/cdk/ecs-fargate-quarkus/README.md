# ecs-fargate-quarkus Project

aws ecr get-login-password --region eu-central-1 --profile cli | docker login --username AWS --password-stdin ${CDK_DEFAULT_ACCOUNT}.dkr.ecr.eu-central-1.amazonaws.com

curl http://ecs-f-ecsfa-1A6PVFQ5AAK83-623426443.eu-central-1.elb.amazonaws.com/greetings


aws ecs update-service --force-new-deployment --cluster ecs-fargate-quarkus-ecsfargatequarkuscluster240BCF38-YvELQUIzTuRx  --service ecs-fargate-quarkus-ecsfargatequarkusserviceService44A7F2A7-ce68g8NzBULd --profile cli