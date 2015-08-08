git pull
mvn clean package -PnoChecks
java -jar $(ls -1rt target/nanobot*.jar)
