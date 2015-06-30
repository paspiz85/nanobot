#!/bin/bash
FOLDER="nanobot"
GIT_URL="https://github.com/paspiz85/nanobot.git"
GIT_BRANCH="feature/collecting"

rm -rf $FOLDER
git clone $GIT_URL $FOLDER
pushd $FOLDER
{
	git checkout $GIT_BRANCH
	mvn clean install
} || {
	popd
	rm -rf $FOLDER
}
