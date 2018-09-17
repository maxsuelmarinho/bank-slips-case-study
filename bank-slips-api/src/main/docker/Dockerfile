FROM openjdk:8-jdk-alpine

MAINTAINER Maxsuel Marinho <maxsuelmarinho@gmail.com>

ENV LANG en_US.utf8
ENV TZ "America/Sao_Paulo"
VOLUME /tmp
VOLUME /app/logs
VOLUME /app/config

RUN apk update && \
    apk add --no-cache \
        gettext \
        bash

ADD bank-slips-api-*.jar /app/app.jar
RUN sh -c 'touch /app/app.jar'
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/docker-entrypoint.sh"]
