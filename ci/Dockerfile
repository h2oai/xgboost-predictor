FROM ubuntu:16.04

RUN apt-get update && \
    apt-get install -y \
        make \
        s3cmd \
        unzip \
        git && \
    rm -rf /var/lib/apt/lists/*

# Add the Jenkins user
RUN \
    groupadd -g 2117 jenkins && \
    useradd jenkins -m -u 2117 -g jenkins

COPY ci/jdk1.8.0_171.zip /usr/opt/
RUN cd /usr/opt && \
    unzip -q jdk1.8.0_171.zip && \
    rm -rf jdk1.8.0_171.zip

ENV JAVA_HOME=/usr/opt/jdk1.8.0_171
ENV PATH=${PATH}:/usr/opt/jdk1.8.0_171/bin

RUN mkdir /gradle-home && \
    chmod a+w /gradle-home

ENV GRADLE_USER_HOME /gradle-home
ENV GRADLE_OPTS -Dorg.gradle.daemon=false
