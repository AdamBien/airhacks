packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.1"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "graviton" {
  tags = {
    environment = "workshops"
    project     = "airhacks.live"
    application = "packer-aws-ebs"
    Name        = "airhacks-packer-graviton"
  }
  region        = "eu-central-1"
  profile       = "cli"
  source_ami    = "ami-06e0f87a4491c1b8b"
  instance_type = "t4g.micro"
  ssh_username  = "ec2-user"
  ami_name      = "airhacks-packer-graviton"
  vpc_id        = "vpc-4730572d"
  //a public subnet
  subnet_id                   = "subnet-c8612ea2"
  associate_public_ip_address = true
  security_group_id           = "sg-081b68a3a39f1443d"

  launch_block_device_mappings {
    device_name = "/dev/sda1"
    volume_size = 30
    volume_type = "gp2"
    //https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ebs-volume-types.html
    delete_on_termination = true
  }
  ami_block_device_mappings {
    device_name  = "/dev/sdb"
    virtual_name = "ephemeral0"
  }
  ami_block_device_mappings {
    device_name  = "/dev/sdc"
    virtual_name = "ephemeral1"
  }
}




build {

  provisioner "file" {
    source      = "./payload/"
    destination = "/tmp/"
  }
  provisioner "shell" {
    inline = [
      "sudo yum install java-11-amazon-corretto-headless -y",
      "sudo mv /tmp/mockend.service /etc/systemd/system/",
      "sudo yum update -y",
      "sudo systemctl enable mockend.service"
    ]

  }
  sources = [
    "source.amazon-ebs.graviton"
  ]
}