git pull
mvn clean package -Dmaven.test.skip=true -Dcheckstyle.skip=true
java -jar $(ls -1rt target/nanobot*.jar)
