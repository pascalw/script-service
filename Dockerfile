FROM java:openjdk-8-jdk

RUN mkdir -p /app/
ADD https://github.com/pascalw/scriptor/releases/download/1.0.0-SNAPSHOT/scriptor-1.0.0-SNAPSHOT.jar /app/scriptor.jar

RUN useradd scriptor
RUN chown -R scriptor:scriptor /app
USER scriptor

ENV PORT=8080
EXPOSE 8080

CMD java -jar /app/scriptor.jar
