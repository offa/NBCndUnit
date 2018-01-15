#!/bin/sh

set -ex

set _JAVA_OPTIONS=-Djavax.net.ssl.trustStorePassword=changeit

# Install Dependencies
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V

# Build and Test
mvn test -B
