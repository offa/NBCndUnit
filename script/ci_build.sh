#!/bin/sh

set -ex

java -version
mvn --version


# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
