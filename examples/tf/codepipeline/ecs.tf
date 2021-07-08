
resource "aws_security_group" "fargate-airhacks-sg-lb" {
  name   = "fargate-airhacks-sg-lb"
  tags = {
     Name = "fargate-airhacks-sg-lb"
  }
  vpc_id = aws_vpc.airhacks.id

  ingress {
   protocol         = "tcp"
   from_port        = 80
   to_port          = 80
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
 
  ingress {
   protocol         = "tcp"
   from_port        = 443
   to_port          = 443
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
 
  egress {
   protocol         = "-1"
   from_port        = 0
   to_port          = 0
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_security_group" "fargate-airhacks-sg-ecs-task" {
  name   = "fargate-airhacks-sg-ecs-task"
  vpc_id = aws_vpc.airhacks.id
 
  ingress {
   protocol         = "tcp"
   from_port        = 8080
   to_port          = 8080
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
 
  egress {
   protocol         = "-1"
   from_port        = 0
   to_port          = 0
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_alb_target_group" "alb_target_group_blue" {
  name        = "alb-target-group-airhacks-blue"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.airhacks.id
  lifecycle {
        create_before_destroy = true
        ignore_changes = [name]
  }
  health_check {
    interval            = 30
    path                = "/health/ready"
    protocol            = "HTTP"
    timeout             = 10
    healthy_threshold   = 2
    unhealthy_threshold = 2
    port = 8080
  }   
  target_type = "ip"
  depends_on = [ aws_alb.airhacks ]
}

 resource "aws_alb_target_group" "alb_target_group_green" {
  name        = "alb-target-group-airhacks-green"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.airhacks.id
  lifecycle {
        create_before_destroy = true
        ignore_changes = [name]
  }
  health_check {
    interval            = 30
    path                = "/q/health/ready"
    protocol            = "HTTP"
    timeout             = 10
    healthy_threshold   = 2
    unhealthy_threshold = 2
    port = 8080
  }   
  target_type = "ip"
  depends_on = [ aws_alb.airhacks ]
}
 
 resource "aws_alb" "airhacks" {
  name            = var.service_name
  subnets         = aws_subnet.public.*.id
  security_groups = [aws_security_group.fargate-airhacks-sg-lb.id]
} 

resource "aws_alb_listener" "airhacks" {
  load_balancer_arn = aws_alb.airhacks.id
  port              = 80
  protocol          = "HTTP"
 
  default_action {
   type = "forward"
   target_group_arn = aws_alb_target_group.alb_target_group_green.arn
  }
}

resource "aws_ecs_cluster" "airhacks" {
  name = var.cluster_name
}

resource "aws_ecs_task_definition" "initial" {
  family            = var.service_name
  // https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-cpu-memory-error.html
  cpu               = 512
  memory            = 1024
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn
  requires_compatibilities = ["FARGATE"]
  network_mode = "awsvpc"
  //the ContainerDefinitions are going to be replaced by the pipeline, only initial definition
  //After the task definition is registered, edit your file to remove the image name and include the <IMAGE1_NAME> placeholder text in the image field.
  //https://docs.aws.amazon.com/codepipeline/latest/userguide/tutorials-ecs-ecr-codedeploy.html
  //https://docs.aws.amazon.com/AmazonECS/latest/APIReference/API_ContainerDefinition.html


  container_definitions = <<TASK_DEFINITION
[
    {
        "name": "${var.service_name}",
        "image": "airhacks/WILL_BE_REPLACED_BY_CODEPIPELINE",
        "essential": true,
        "healthCheck": {
              "retries": 3,
              "command": [
                  "CMD-SHELL",
                  "curl -f http://localhost:8080/q/health/live || exit 1"
              ],
              "timeout": 5,
              "interval": 30
          },          
        "portMappings": [
            {
                "containerPort": 8080,
                "hostPort": 8080
            }
        ]
    }
]
TASK_DEFINITION

}
//https://docs.aws.amazon.com/AmazonECS/latest/developerguide/service_definition_parameters.html
resource "aws_ecs_service" "airhacks" {
  name                =  var.service_name
  cluster             =  aws_ecs_cluster.airhacks.id
  desired_count       = var.aws_ecs_service_desired_count
  task_definition     = aws_ecs_task_definition.initial.arn
  launch_type = "FARGATE"
  network_configuration {
      security_groups  = [aws_security_group.fargate-airhacks-sg-ecs-task.id]
      subnets          = aws_subnet.private.*.id
    }  
  load_balancer {
    target_group_arn = aws_alb_target_group.alb_target_group_green.arn
    container_name   = var.service_name
    container_port   = "8080"
  }
  depends_on = [ aws_alb_listener.airhacks ]

  deployment_controller{
    type = "CODE_DEPLOY"
  }
}