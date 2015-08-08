git pull
call mvn clean package -PnoChecks
FOR %%f in (target\nanobot*.jar) DO java -jar %%f
