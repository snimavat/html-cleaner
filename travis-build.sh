#!/bin/bash

rm -rf *.zip

./grailsw refresh-dependencies --non-interactive

echo "### Running tests"

./grailsw test-app --non-interactive
./grailsw package-plugin --non-interactive
./grailsw maven-install --non-interactive

if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_REPO_SLUG == "snimavat/html-cleaner" && $TRAVIS_PULL_REQUEST == 'false' ]]; then
	echo "### publishing plugin to grails central"
	./grailsw publish-plugin --allow-overwrite --non-interactive --noScm

else
  echo "Not on master branch, so not publishing"
  echo "TRAVIS_BRANCH: $TRAVIS_BRANCH"
  echo "TRAVIS_REPO_SLUG: $TRAVIS_REPO_SLUG"
  echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
fi	


