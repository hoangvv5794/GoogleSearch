FROM mcr.microsoft.com/playwright/java:focal

COPY target/lib ./lib
COPY src/main/resources/* .
COPY target/GoogleSearchAPI-1.0-SNAPSHOT.jar ./lib
# set the startup command to execute the jar
CMD ["java", "-cp", "lib/*:dist/*", "com.viettel.vtcc.crawler.search.google.GoogleAPI"]