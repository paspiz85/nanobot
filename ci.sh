#!/bin/bash
FOLDER="/tmp/nanobot"
LOGFILE="/tmp/nanobot.log"
GIT_URL="https://github.com/paspiz85/nanobot.git"
GIT_BRANCH="master"
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home

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
echo "User: $USER" > $LOGFILE
git clone $GIT_URL $FOLDER >> $LOGFILE
pushd $FOLDER >> $LOGFILE
{
	pwd >> $LOGFILE
	git checkout $GIT_BRANCH >> $LOGFILE
	mvn --version >> $LOGFILE
	mvn clean install >> $LOGFILE
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
