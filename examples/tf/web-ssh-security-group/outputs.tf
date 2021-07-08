output "aws_security_group_id" {
    value = aws_security_group.ec2.id
}
output "aws_security_group_arn" {
    value = aws_security_group.ec2.arn
}

output "aws_security_group_name" {
    value = aws_security_group.ec2.name
}