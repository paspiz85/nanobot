##Developer Guide

###Requirements
* Git
* JDK 8
* Maven

###Instructions
Download source code with:
```
git clone https://github.com/paspiz85/nanobot.git
```
Move into "nanobot" folder.

##### Complete build
```
mvn clean install
```
Skipping tests and checkstyle:
```
mvn clean install -Dmaven.test.skip=true -Dcheckstyle.skip=true
```

##### Running from source
```
mvn clean compile exec:exec
```

##### Debugging
```
mvnDebug clean compile exec:exec
```

##### Releasing
Check that your SSH key (~/.ssh/id_rsa.pub) is configured on GitHub (https://github.com/settings/ssh).

To prepare a release (POM changes and tag create):
```
mvn -B release:clean release:prepare
```
To perform:
```
mvn release:perform
```


mvn release:update-versions

