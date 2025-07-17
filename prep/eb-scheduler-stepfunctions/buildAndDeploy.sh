#!/bin/sh
set -e
mvn -DskipTests clean package && cdk deploy