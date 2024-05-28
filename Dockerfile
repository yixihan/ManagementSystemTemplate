FROM openjdk:8
EXPOSE 18080

MAINTAINER yixihan<yixihan20010617@gmail.com>

ENV TZ 'Asia/Shanghai'
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.en
ENV LC_ALL en_US.UTF-8

VOLUME /tmp

ADD target/*.jar  /app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]