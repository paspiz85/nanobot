mvn clean install -Dmaven.test.skip=true -Dcheckstyle.skip=true
#java -jar  $(ls -1rt target/*.jar)
$JARFILE=(ls target\*.jar).Name
java -jar target\$JARFILE
