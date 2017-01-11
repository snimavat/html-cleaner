#!/bin/bash

set -e

echo "### Running plugin tests ###"
(cd ./html-cleaner-plugin && ./gradlew clean check assemble --stacktrace)

echo "### Running demo app tests ###"
(cd ./html-cleaner-test-app && ./gradlew clean check --stacktrace)


if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_REPO_SLUG == "snimavat/html-cleaner" && $TRAVIS_PULL_REQUEST == 'false' ]]; then
	echo "### publishing plugin to grails central"
	(cd ./html-cleaner-plugin && ./gradlew bintrayUpload)
	
else
  echo "Not on master branch, so not publishing"
  echo "TRAVIS_BRANCH: $TRAVIS_BRANCH"
  echo "TRAVIS_REPO_SLUG: $TRAVIS_REPO_SLUG"
  echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
fi	


