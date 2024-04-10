#!/usr/bin/env sh

if [[ $# == 0 ]]; then
	printf "\033[33;1m%s\033[m\n" "[WARNING] No parameters!"
	exit 1
fi

EXIT=0

for VAR in "$@"; do
	if [[ "$(env | grep $VAR)" == "" ]]; then
		printf "\033[31;1m%s\033[m\n" "[ERROR] '$VAR' is empty!"
		EXIT=1
	fi
done

if [[ $EXIT == 1 ]]; then
	exit 1
fi
