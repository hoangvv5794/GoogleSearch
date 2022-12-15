FROM mcr.microsoft.com/playwright/java:focal

RUN mkdir ./answer
COPY target/lib ./app/lib
COPY src/main/resources/* ./app
COPY target/GoogleSearchAPI-1.0-SNAPSHOT.jar ./app/lib
# set the startup command to execute the jar
CMD ["java", "-cp", "/app/lib/*:/app/dist/*", "com.viettel.vtcc.crawler.search.google.GoogleAPI"]