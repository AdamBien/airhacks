resource "aws_eip" "nat_eip" {
  count = length(aws_subnet.private)
  tags = {
    Name = "codepipeline-private-${count.index}" 
  }
  vpc = true
}

resource "aws_nat_gateway" "private" {
  tags = {
    Name = "codepipeline-private-${count.index}"
  }
  count         = length(aws_subnet.private)
  allocation_id = element(aws_eip.nat_eip.*.id,count.index)
  subnet_id     = element(aws_subnet.public.*.id,count.index)
}

resource "aws_route_table" "private" {
  tags = {
      Name = "codepipeline-private-${count.index}"
  }
  count  = length(aws_subnet.private)
  vpc_id = aws_vpc.airhacks.id
  //https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = element(aws_nat_gateway.private.*.id,count.index)
  }
}

resource "aws_route_table_association" "private" {
  count          = length(aws_subnet.private)
  subnet_id      = element(aws_subnet.private.*.id,count.index)
  route_table_id = element(aws_route_table.private.*.id,count.index)
}