#!/usr/bin/env sh

cat gradle.properties | grep project_version | awk -F= '{print $2}'
