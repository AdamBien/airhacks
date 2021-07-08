# custom airhacks AMIs


# used image

aws ec2 describe-images --image-ids ami-057c9f9ed7e804474


# search for images
aws ec2 describe-images \
    --owners amazon \
    --filters "Name=root-device-type,Values=ebs" --profile terraform

# connect to instance

ssh -i airhacks_ec2.pem ec2-user@ec2-3-65-59-205.eu-central-1.compute.amazonaws.com