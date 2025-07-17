#!/bin/sh
set -e
mvn -DskipTests clean package && cdk deploy --all --require-approval=never