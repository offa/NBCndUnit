#!/bin/sh

set -ex

mvn verify -Dmaven.javadoc.skip=true -B -V --no-transfer-progress
