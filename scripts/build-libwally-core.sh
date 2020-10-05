#! /usr/bin/env bash

if [ "$(uname)" == "Darwin" ]; then
  echo "Build libwally-core MacOS"
  ./scripts/build-libwally-core-mac.sh
else
  echo "Build libwally-core Linux"
  ./scripts/build-libwally-core-linux.sh
fi
