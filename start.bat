git pull
call mvn clean package -Dmaven.test.skip=true -Dcheckstyle.skip=true
FOR %%f in (target\nanobot*.jar) DO java -jar %%f
