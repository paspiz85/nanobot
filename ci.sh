#!/bin/bash
FOLDER="/tmp/nanobot"
GIT_URL="https://github.com/paspiz85/nanobot.git"
GIT_BRANCH="feature/collecting"

function notify {
	OS=$(uname)
	if [[ $OS == 'Darwin' ]]; then
		osascript -e 'tell application "Finder"' -e 'activate' -e "display dialog \"$1\"" -e 'end tell'
		#osascript -e "display dialog \"$1\""
	elif [[ $OS == 'Linux' ]]; then
		echo $1
	else
		echo $1
	fi
}

rm -rf $FOLDER
git clone $GIT_URL $FOLDER
pushd $FOLDER
{
	git checkout $GIT_BRANCH
	mvn clean install
	RESULT=$?
	if [[ $RESULT != 0 ]]; then
		notify "Build nanobot failed"
	#else
		#notify "Build nanobot OK"
	fi
} || {
	popd
	rm -rf $FOLDER
}
