#!/usr/bin/env sh

set -x

CHANGELOG_CONFIG_PATH="chglog/conf-gitlab-1"
if [ "$CI_COMMIT_BRANCH" == "develop" ]; then
  export MAJOR="develop"
else
  MAJOR=$(./scripts/utils/getMajor.sh)
  export MAJOR
fi

pullChangelogTemplateFiles() {
	CHGLOG_TARGET=".chglog"
	mkdir -p $CHGLOG_TARGET
	gsutil cp -R gs://equo-ci-files/master/${CHANGELOG_CONFIG_PATH} $CHGLOG_TARGET
}

createChangelog() {
	PROJECT_VERSION=$(./scripts/utils/getProjectVersion.sh)
	TAG_PATTERN="v"
	git-chglog -c .${CHANGELOG_CONFIG_PATH}/config.yml \
		-t .${CHANGELOG_CONFIG_PATH}/CHANGELOG.tpl.md \
		--tag-filter-pattern "^${TAG_PATTERN}" \
		--repository-url $CI_PROJECT_URL \
		--no-case \
		--next-tag $TAG_PATTERN$PROJECT_VERSION $TAG_PATTERN$PROJECT_VERSION \
		>$CHANGELOG_FILENAME

	gsutil cat gs://$GCS_BUCKET/core/$MAJOR/$CHANGELOG_FILENAME >>$CHANGELOG_FILENAME
}

publishChangelog() {
	gsutil \
		-h "Cache-Control":" private" \
		-h "Content-Type":" text/plain" \
		cp -a public-read $CHANGELOG_FILENAME gs://$GCS_BUCKET/core/$MAJOR/

	CORSFILE="cors.json"
	echo '[{"method": ["GET"], "origin": ["*"]}]' >$CORSFILE
	gsutil cors set $CORSFILE gs://${GCS_BUCKET}
}

main() {
	./scripts/utils/gcloudAuth.sh
	pullChangelogTemplateFiles
	createChangelog
	publishChangelog
}

./scripts/utils/checkVariables.sh CI_PROJECT_URL GCS_BUCKET CHANGELOG_FILENAME MAJOR
if [[ $? == 1 ]]; then
	exit 1
fi

main
