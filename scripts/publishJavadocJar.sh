#!/usr/bin/env sh

set -x

main() {
	./scripts/utils/gcloudAuth.sh
	./gradlew -q javadocJar
	mv core/build/libs/core-*-javadoc.jar core/build/libs/javadoc.jar
	gsutil cp -a public-read core/build/libs/javadoc.jar gs://$GCS_BUCKET/core
}

./scripts/utils/checkVariables.sh GCS_BUCKET
if [[ $? == 1 ]]; then
	exit 1
fi

main
