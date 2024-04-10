FROM eclipse-temurin:17-jdk-alpine

CMD ["/bin/sh"]

COPY --from=golang:1.22-alpine /usr/local/go/ /usr/local/go/
COPY --from=registry.gitlab.com/haynes/jacoco2cobertura:1.0.10 /opt/cover2cover.py /opt

ENV GOROOT /usr/local/go
ENV GOPATH /go
ENV GOBIN $GOPATH/bin
ENV PATH $GOBIN:$PATH
ENV PATH $GOROOT/bin:$PATH

RUN set -o errexit -o nounset \
    && mkdir -p ${GOPATH}/src ${GOPATH}/bin \
    && echo "Installing build dependencies" \
    && apk add xz curl tar python3 python3-dev git \
    && echo "Downloading Google Cloud SDK" \
    && curl https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-cli-470.0.0-linux-x86_64.tar.gz | tar -xz \
    && /google-cloud-sdk/install.sh -q \
    && go install github.com/git-chglog/git-chglog/cmd/git-chglog@v0.15.4

ENV PATH $PATH:/google-cloud-sdk/bin
ENV GOOGLE_CLOUD_HOME /google-cloud-sdk

RUN set -o errexit -o nounset \
    && echo "Testing gsutil installation" \
    && gsutil --version \
    && echo "Testing go installation" \
    && go version \
    && echo "Testing git-chglog installation" \
    && git-chglog --help

