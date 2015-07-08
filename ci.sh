#!/bin/bash
FOLDER="/tmp/nanobot"
LOGFILE="/tmp/nanobot.log"
GIT_URL="https://github.com/paspiz85/nanobot.git"
GIT_BRANCH="feature/collecting"

function notify {
	OS=$(uname)
	if [[ $OS == 'Darwin' ]]; then
		osascript -e 'tell application "Finder"' -e 'activate' -e "display dialog \"$1\"" -e 'end tell'
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
	mvn clean install > $LOGFILE
	RESULT=$?
	if [[ $RESULT != 0 ]]; then
		notify "Build nanobot failed: see $LOGFILE"
	#else
		#notify "Build nanobot OK"
	fi
} || {
        notify "Build nanobot exception"
}
popd
rm -rf $FOLDER
