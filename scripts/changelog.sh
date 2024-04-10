#!/usr/bin/env sh

set -x

CHANGELOG_CONFIG_PATH="chglog/conf-gitlab-1"
if [ "$CI_COMMIT_BRANCH" == "develop" ]; then
	export MAJOR="develop"
else
	export MAJOR=$(./scripts/utils/getMajor.sh)
fi

pullChangelogTemplateFiles() {
	CHGLOG_TARGET=".chglog"
	mkdir -p $CHGLOG_TARGET
	gsutil cp -R gs://equo-ci-files/master/${CHANGELOG_CONFIG_PATH} $CHGLOG_TARGET
}

createChangelog() {
	TAG_PATTERN="v"
	PROJECT_VERSION=$(./scripts/utils/getProjectVersion.sh)
	CLOUD_CHANGELOG=$(gsutil cat gs://$GCS_BUCKET/core/$MAJOR/$CHANGELOG_FILENAME)

	echo "$CLOUD_CHANGELOG" | grep "$TAG_PATTERN$PROJECT_VERSION"
	if [[ $? == 1 ]]; then
		git-chglog \
			--config .${CHANGELOG_CONFIG_PATH}/config.yml \
			--template .${CHANGELOG_CONFIG_PATH}/CHANGELOG.tpl.md \
			--tag-filter-pattern "^${TAG_PATTERN}" \
			--repository-url $CI_PROJECT_URL \
			--no-case \
			--next-tag $TAG_PATTERN$PROJECT_VERSION $TAG_PATTERN$PROJECT_VERSION \
			>$CHANGELOG_FILENAME

		echo "$CLOUD_CHANGELOG" >>$CHANGELOG_FILENAME
	fi
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
	if [[ "$PUBLISH_CHANGELOG" == "true" ]]; then
		publishChangelog
	fi
}

./scripts/utils/checkVariables.sh CI_PROJECT_URL GCS_BUCKET CHANGELOG_FILENAME MAJOR
if [[ $? == 1 ]]; then
	exit 1
fi

main
