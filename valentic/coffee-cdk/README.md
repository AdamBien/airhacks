# Deployment for AWS Lambda with AWS Cloud Development Kit (CDK) Boilerplate

This is a blank CDK-only project for AWS Lambda "function URL" deployment.
The archive `function.zip` has to already exist. 

The template [https://github.com/AdamBien/aws-quarkus-lambda-cdk-plain](aws-quarkus-lambda-cdk-plain) 
builds and packages a Quarkus-based AWS Lambda and provides additional options as: ALB, Http API and REST API. 

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Installation

1. Install [AWS CDK CLI](https://docs.aws.amazon.com/cdk/latest/guide/getting_started.html)
2. [`cdk boostrap --profile YOUR_AWS_PROFILE`](https://docs.aws.amazon.com/cdk/latest/guide/bootstrapping.html)

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

## in action

[![Infrastructure as Java Code (IaJC): Setting AWS System Manager Parameter](https://i.ytimg.com/vi/eTG7EV1ThqQ/mqdefault.jpg)](https://www.youtube.com/embed/eTG7EV1ThqQ?rel=0)



See you at: [airhacks.live](https://airhacks.live)