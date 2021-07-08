resource "aws_s3_bucket" "codepipeline" {
  bucket =  var.s3_bucket_codepipeline_name
  acl    = "private"
  force_destroy = "true"
}
