#!/usr/bin/env sh

set -eu

APP_HOME=$(CDPATH='' cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_PROPERTIES="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "$WRAPPER_PROPERTIES" ]; then
  echo "ERROR: Missing $WRAPPER_PROPERTIES" >&2
  exit 1
fi

DISTRIBUTION_URL=$(sed -n 's/^distributionUrl=//p' "$WRAPPER_PROPERTIES" | tail -n1)
if [ -z "$DISTRIBUTION_URL" ]; then
  echo "ERROR: distributionUrl is not configured in gradle-wrapper.properties" >&2
  exit 1
fi

DISTRIBUTION_URL=$(printf '%s' "$DISTRIBUTION_URL" | sed 's#\\:#:#g')
DIST_FILE=$(basename "$DISTRIBUTION_URL")
EXPECTED_VERSION=$(printf '%s' "$DIST_FILE" | sed -E 's/^gradle-([0-9.]+)-.*/\1/')

if command -v gradle >/dev/null 2>&1; then
  INSTALLED_VERSION=$(gradle --version 2>/dev/null | sed -n 's/^Gradle \([0-9.]*\)$/\1/p' | head -n1 || true)
  if [ "$INSTALLED_VERSION" = "$EXPECTED_VERSION" ]; then
    exec gradle "$@"
  fi
fi

CACHE_DIR="$APP_HOME/.gradle-wrapper"
ZIP_PATH="$CACHE_DIR/$DIST_FILE"
EXTRACT_DIR="$CACHE_DIR/gradle-$EXPECTED_VERSION"
GRADLE_BIN="$EXTRACT_DIR/bin/gradle"

mkdir -p "$CACHE_DIR"

if [ ! -x "$GRADLE_BIN" ]; then
  if [ ! -f "$ZIP_PATH" ]; then
    echo "Downloading Gradle $EXPECTED_VERSION from $DISTRIBUTION_URL" >&2
    if command -v curl >/dev/null 2>&1; then
      curl -fsSL "$DISTRIBUTION_URL" -o "$ZIP_PATH"
    elif command -v wget >/dev/null 2>&1; then
      wget -q "$DISTRIBUTION_URL" -O "$ZIP_PATH"
    else
      echo "ERROR: Neither curl nor wget is available to download Gradle." >&2
      exit 1
    fi
  fi

  rm -rf "$EXTRACT_DIR"
  if command -v unzip >/dev/null 2>&1; then
    unzip -q "$ZIP_PATH" -d "$CACHE_DIR"
  else
    echo "ERROR: unzip command is required to unpack Gradle distribution." >&2
    exit 1
  fi
fi

if [ ! -x "$GRADLE_BIN" ]; then
  echo "ERROR: Gradle binary not found at $GRADLE_BIN after extraction." >&2
  exit 1
fi

exec "$GRADLE_BIN" "$@"
