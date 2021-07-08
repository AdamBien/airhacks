
https://docs.aws.amazon.com/codecommit/latest/userguide/setting-up-ssh-unixes.html#setting-up-ssh-unixes-keys

ssh-keygen -t rsa -b 4096

~/.ssh/config

Host git-codecommit.*.amazonaws.com
  User APKOUIANMFAOUNOTEXIST
  IdentityFile ~/.ssh/codecommit_rsa%