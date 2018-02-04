#!/bin/sh

set -ex

# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
