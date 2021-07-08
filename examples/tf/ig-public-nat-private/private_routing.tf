resource "aws_eip" "nat_eip" {
  tags = {
    Name =  "${var.vpc_name}-private" 
  }
  vpc = true
}

resource "aws_nat_gateway" "private" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public.id
  depends_on    = [aws_internet_gateway.airhacks]
}

resource "aws_route_table" "private" {
  tags = {
    Name =  "${var.vpc_name}-private"
  }
  vpc_id = aws_vpc.airhacks.id
  //https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.private.id
  }
}

resource "aws_route_table_association" "private" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.private.id
}