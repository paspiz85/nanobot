##Developer Guide

###Requirements
* Git
* JDK 8
* Maven
Check that your SSH key (~/.ssh/id_rsa.pub) is configured on [GitHub] (https://github.com/settings/ssh).

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

##### Running
On Windows:
```
$JARFILE=(ls target\*.jar).Name
java -jar target\$JARFILE
```
On other OS:
```
java -jar $(ls -1rt target/nanobot*.jar)
```

##### Running from source
```
mvn clean compile exec:exec
```

##### Debugging
```
mvn clean compile exec:exec -Ddebug=true
```

##### Releasing
To list tags:
```
git tag -l
```

To prepare a release (POM changes and tag create):
```
mvn release:clean release:prepare
```

Then continue release on [GitHub] (https://github.com/paspiz85/nanobot/releases) and edit tag adding description and executable jar.
ATTENTION: name resease with v prefix. For example 1.1.0 must be v1.1.0

##### Rollback Release
To delete a tag:
```
git tag -d <tagname>
git push origin :refs/tags/<tagname>
```

Then continue deleting release on [GitHub] (https://github.com/paspiz85/nanobot/releases).
