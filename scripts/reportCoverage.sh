#!/usr/bin/env sh

set -x

main() {
	jacoco_paths=$(
		find * -path "**/src/main/java" -type d |
			sed -e 's@^@'"$CI_PROJECT_DIR"'/@'
	)

	python /opt/cover2cover.py \
		build/reports/jacoco/jacocoRootReport/jacocoRootReport.xml $jacoco_paths \
		>$COVERAGE_FILENAME

	./gradlew -q print-coverage
}

./scripts/utils/checkVariables.sh CI_PROJECT_DIR COVERAGE_FILENAME
if [[ $? == 1 ]]; then
	exit 1
fi

main
