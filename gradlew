#!/usr/bin/env bash

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a softlink
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    PRG=`readlink "$PRG"`
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# Tell cygwin the Java location to find cygpath utility
if [ -n "$JAVA_HOME" ] ; then
    javaexe="`find \"$JAVA_HOME\" -type f -name java`"
    if [ -z "$javaexe" ] ; then
        echo "JAVA_HOME is not defined correctly" >&2
        exit 1
    fi
    javabin="`dirname \"$javaexe\"`"
    PATH="$javabin:$PATH"
fi

# Determine OS
OSTYPE=`uname -s`

# Provide a command to get the real path of a file.
realpath() {
  case $OSTYPE in
    (CYGWIN*)
      echo $(cygpath -w "$1")
      ;;
    (*)
      echo "$(cd "$(dirname "$1")" && pwd)/$(basename "$1")"
      ;;
  esac
}

if [ ! -x "$APP_HOME/gradlew" ] ; then
   echo "Error: gradlew not executable"
   exit 1
fi

exec "$APP_HOME/gradlew.bat" "$@"
