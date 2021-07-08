terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

data "aws_security_group" "ec2"{
  name = "airhacks-web-ssh-ec2"
}

data "aws_ami" "amazon-ebs-graviton-mockend" {
  owners = ["self"]
  most_recent      = true
  filter {
    name   = "name"
    values = ["airhacks-packer-graviton"]
  }
}

locals {
  region = var.aws_region
  key_name = "airhacks_ec2"
}
provider "aws" {
  region = local.region
  profile = var.aws_profile
}

resource "tls_private_key" "airhacks" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "generated_key" {
  key_name   = local.key_name
  public_key = tls_private_key.airhacks.public_key_openssh
  tags = {
    Name = "airhacks"
  }
}

resource "aws_network_interface" "airhacks" {
  subnet_id       = var.subnet_id
  tags = {
      Name = "airhacks"
  }
  security_groups = [data.aws_security_group.ec2.id]
}


resource "aws_instance" "airhacks" {
  ami           = data.aws_ami.amazon-ebs-graviton-mockend.id
  instance_type = var.instance_type
  key_name      = aws_key_pair.generated_key.key_name
  
  network_interface {
    network_interface_id = aws_network_interface.airhacks.id
    device_index         = 0
  }
  tags = {
    Name = "airhacks"
    Purpose = "debugging"
  }
}

resource "aws_eip" "airhacks" {
  instance = aws_instance.airhacks.id
  vpc      = true
}

resource "local_file" "airhacks" {
    content     = tls_private_key.airhacks.private_key_pem
    filename = "${path.module}/airhacks_ec2.pem"
    file_permission = "400"
}

output "private_key"{
  value = tls_private_key.airhacks.private_key_pem
}

output "public_dns"{
  value = aws_eip.airhacks.public_dns
}

output "public_ip"{
  value = aws_eip.airhacks.public_ip
}

output "ssh" {
  value = "ssh -i airhacks_ec2.pem ec2-user@${aws_eip.airhacks.public_dns}"
}