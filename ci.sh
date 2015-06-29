#!/bin/bash
rm -rf nanobot
git clone https://github.com/paspiz85/nanobot.git
cd nanobot
{
	mvn clean install
} || {
	cd ..
	rm -rf nanobot
}
