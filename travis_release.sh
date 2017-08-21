#!/bin/bash

if [ -z "$TRAVIS_TAG" ]; then echo "Not executing travis_release because this is not a tagged build" && exit 0; fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
FILE="$DIR/build.properties"

sed -i "s/^version=.*\$/version=$TRAVIS_TAG-$TRAVIS_BUILD_NUMBER/" "$FILE"
