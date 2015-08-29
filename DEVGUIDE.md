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

Skipping tests, checkstyle and install:
```
mvn clean package -Dmaven.test.skip=true -Dcheckstyle.skip=true
```

##### Running
On Windows (double % in .bat):
```
FOR %f in (target\nanobot*.jar) DO java -jar %f
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
mvn release:clean release:prepare && \
mvn github-release:release -D$(grep scm.tag=v release.properties) && \
mvn release:clean
```


##### Rollback Release
To delete a tag:
```
git tag -d <tagname>
git push origin :refs/tags/<tagname>
```

Then continue deleting release on [GitHub] (https://github.com/paspiz85/nanobot/releases).
