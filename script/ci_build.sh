#!/bin/sh

set -ex

cat /etc/lsb-release


# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
