#!/bin/sh

java -version
mvn --version

set -ex


# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
