/*
resource "aws_kms_key" "airhacks" {
  description              = "key to encrypt S3 bucket for codepipeline"
  customer_master_key_spec = "SYMMETRIC_DEFAULT"
  is_enabled               = true
  enable_key_rotation      = false
  deletion_window_in_days  = 30
}

resource "aws_kms_alias" "airhacks" {
  name          = "alias/codepipeline-key-alias"
  target_key_id = aws_kms_key.airhacks.key_id
}
*/