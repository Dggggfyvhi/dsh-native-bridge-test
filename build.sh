#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
export ANDROID_HOME=${ANDROID_HOME:-/opt/android-sdk}
./gradlew --no-daemon assembleDebug
