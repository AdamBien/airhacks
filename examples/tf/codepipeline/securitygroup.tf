resource "aws_security_group" "airhacks" { 
  name = "airhacks-codebuild-security-group" 
  vpc_id = aws_vpc.airhacks.id
  ingress { 
    from_port = 443 
    to_port = 443 
    protocol = "tcp" 
    cidr_blocks = ["0.0.0.0/0"] 
  } 

  egress { 
    from_port = 0 
    to_port = 0 
    protocol = "-1" 
    cidr_blocks = ["0.0.0.0/0"] 
  } 
}