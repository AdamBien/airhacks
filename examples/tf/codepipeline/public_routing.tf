resource "aws_eip" "elastic-ip-airhacks" {
  tags = {
    Name = "codepipeline-public" 
  }
  vpc        = true
  depends_on = [aws_internet_gateway.airhacks]
}

resource "aws_internet_gateway" "airhacks" {
  vpc_id = aws_vpc.airhacks.id
   tags = {
      Name = "codepipeline"
  }
}

resource "aws_route_table" "public" {
  tags = {
      Name = "codepipeline-public"
  }
  vpc_id = aws_vpc.airhacks.id
}
 
resource "aws_route" "public" {
  route_table_id         = aws_route_table.public.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.airhacks.id
}
 
resource "aws_route_table_association" "public" {
  count          = length(aws_subnet.public)
  subnet_id      = element(aws_subnet.public.*.id,count.index)
  route_table_id = aws_route_table.public.id
}

