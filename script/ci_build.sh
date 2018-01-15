#!/bin/sh

set -ex

# Workaround for broken JDK9 (see #48)
if java -fullversion 2>&1 | grep -q '"9.';
then
    wget https://circle-downloads.s3.amazonaws.com/circleci-images/cache/linux-amd64/openjdk-9-slim-cacerts \
        -O /etc/ssl/certs/java/cacerts
fi


# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
