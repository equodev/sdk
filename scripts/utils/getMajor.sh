#!/usr/bin/env sh

if [[ "$MAJOR" != "" ]]; then
	exit 0
fi

PROJECT_VERSION=$(./scripts/utils/getProjectVersion.sh)

MAJOR=$(echo $PROJECT_VERSION | awk -F. '{print $1}')
echo $MAJOR
