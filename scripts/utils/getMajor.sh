#!/usr/bin/env sh

if [[ "$MAJOR" != "" ]]; then
	exit 0
fi

PROJECT_VERSION=$(cat gradle.properties | grep project_version | awk -F= '{print $2}')

MAJOR=$(echo $PROJECT_VERSION | awk -F. '{print $1}')
echo $MAJOR
