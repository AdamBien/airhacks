data "aws_availability_zones" "available" {
}
resource "aws_vpc" "airhacks" {
  cidr_block       = var.cidr_block
  instance_tenancy = "default"
  enable_dns_hostnames = true
  enable_dns_support = true
  
  tags = {
    Name = var.vpc_name
  }
}

resource "aws_subnet" "public" {
  count = length(data.aws_availability_zones.available.names)
  vpc_id     = aws_vpc.airhacks.id
  cidr_block = cidrsubnet(var.cidr_block,8,count.index)

  tags = {
    Name = "codepipeline-public-subnet-${count.index}"
  }
  availability_zone       = data.aws_availability_zones.available.names[count.index]
}


resource "aws_subnet" "private" {
  count = length(data.aws_availability_zones.available.names)
  vpc_id     = aws_vpc.airhacks.id
  cidr_block = cidrsubnet(var.cidr_block,8,count.index+3)
  tags = {
    Name = "codepipeline-private-subnet-${count.index}"
  }
   availability_zone       = data.aws_availability_zones.available.names[count.index]
}

