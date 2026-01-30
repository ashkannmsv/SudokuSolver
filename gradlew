#!/usr/bin/env bash

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

SCRIPT_DIR=dirname ""
SCRIPT_DIR=cd "" > /dev/null && pwd

# Resolve distribution URL
GRADLE_DIST_URL='https://services.gradle.org/distributions/gradle-8.4-bin.zip'

# Determine os and arch
os_type=uname -s | tr '[:upper:]' '[:lower:]'
os_arch=uname -m | tr '[:upper:]' '[:lower:]'

# Download gradle if not exists
GRADLE_HOME="C:\Users\ASUS/.gradle"
GRADLE_BIN_DIR="/wrapper/dists/gradle-8.4-bin/gradle-8.4/bin"

if [ ! -x "/gradle" ]; then
    echo "Downloading Gradle 8.4..."
    mkdir -p "/wrapper/dists"
    cd "/wrapper/dists"
    if ! curl -L -o gradle-8.4-bin.zip ""; then
        echo "Failed to download Gradle"
        exit 1
    fi
    unzip -q gradle-8.4-bin.zip
    rm gradle-8.4-bin.zip
fi

# Execute gradle
exec "/gradle" "$@"
