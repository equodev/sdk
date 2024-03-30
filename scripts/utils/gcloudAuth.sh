#!/usr/bin/env sh

main() {
	SERVICE_ACCOUNT=$service_account
	CREDENTIALS_FILENAME="credentials.json"
	echo $SERVICE_ACCOUNT | base64 -d >$CREDENTIALS_FILENAME
	gcloud auth activate-service-account --key-file $CREDENTIALS_FILENAME
}

./scripts/utils/checkVariables.sh service_account
if [[ $? == 1 ]]; then
	exit 1
fi

main
