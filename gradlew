#!/usr/bin/env sh
set -e
DIR="$(dirname "$0")"
GRADLEW_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$GRADLEW_JAR" ]; then
  echo "Gradle wrapper JAR not found. Run 'gradle wrapper' locally to generate it." >&2
  exit 1
fi
exec java -jar "$GRADLEW_JAR" "$@"
