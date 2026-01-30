#!/usr/bin/env bash
GRADLE_URL=https://services.gradle.org/distributions/gradle-8.4-bin.zip
GRADLE_HOME=$HOME/.gradle/gradle-8.4
if [ ! -d "$GRADLE_HOME/bin" ]; then
  mkdir -p $HOME/.gradle
  cd $HOME/.gradle
  curl -L -O $GRADLE_URL
  unzip -q gradle-8.4-bin.zip
  rm gradle-8.4-bin.zip
fi
exec $GRADLE_HOME/bin/gradle "$@"
