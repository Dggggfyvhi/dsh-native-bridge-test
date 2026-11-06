#!/usr/bin/env bash
# Minimal wrapper: delegates to system gradle if present.
cd "$(dirname "$0")"
if [ -z "$ANDROID_HOME" ]; then export ANDROID_HOME=/opt/android-sdk; fi
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "No gradle installed. Installing via apt..."
  apt-get update || true
  apt-get install -y gradle || true
  exec gradle "$@"
fi
