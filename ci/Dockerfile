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

COPY ci/zulu7.56.0.11-ca-jdk7.0.352-linux_x64.zip /usr/opt/
RUN cd /usr/opt && \
    unzip -q zulu7.56.0.11-ca-jdk7.0.352-linux_x64.zip && \
    rm -rf zulu7.56.0.11-ca-jdk7.0.352-linux_x64.zip

ENV JAVA_HOME=/usr/opt/zulu7.56.0.11-ca-jdk7.0.352-linux_x64
ENV PATH=${PATH}:/usr/opt/zulu7.56.0.11-ca-jdk7.0.352-linux_x64/bin

RUN mkdir /gradle-home && \
    chmod a+w /gradle-home

ENV GRADLE_USER_HOME /gradle-home
ENV GRADLE_OPTS -Dorg.gradle.daemon=false
