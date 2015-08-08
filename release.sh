mvn release:clean release:prepare && \
mvn github-release:release -D$(grep scm.tag= release.properties) && \
mvn release:clean