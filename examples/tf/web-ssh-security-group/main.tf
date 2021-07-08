resource "aws_security_group" "ec2" { 
  name = "airhacks-web-ssh-ec2" 
  vpc_id = var.vpc_id
}

resource "aws_security_group_rule" "ssh" {
  type                  = "ingress"
  from_port             = 22 
  to_port               = 22 
  protocol              = "tcp" 
  cidr_blocks           = ["0.0.0.0/0"]
  security_group_id     = aws_security_group.ec2.id
}

resource "aws_security_group_rule" "web" {
  type                  = "ingress"
  from_port             = 8080 
  to_port               = 8080 
  protocol              = "tcp" 
  cidr_blocks           = ["0.0.0.0/0"]
  security_group_id     = aws_security_group.ec2.id
}

resource "aws_security_group_rule" "egress" {
  type                  = "egress"
  to_port               = 0
  protocol              = "-1"
  from_port             = 0
  cidr_blocks           = ["0.0.0.0/0"]
  security_group_id     = aws_security_group.ec2.id
}
