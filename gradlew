#!/usr/bin/env bash

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Determine OS
OSTYPE=$(uname -s)

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Find gradle wrapper jar
GRADLE_JAR="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_JAR" ]; then
    echo "Error: gradle-wrapper.jar not found at $GRADLE_JAR"
    exit 1
fi

# Find gradle wrapper properties
GRADLE_PROPS="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "$GRADLE_PROPS" ]; then
    echo "Error: gradle-wrapper.properties not found at $GRADLE_PROPS"
    exit 1
fi

# Execute gradle
exec java $DEFAULT_JVM_OPTS -classpath "$GRADLE_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
