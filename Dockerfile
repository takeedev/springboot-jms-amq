FROM openjdk:11-jre-slim

# กำหนดเวอร์ชันของ ActiveMQ
ENV ACTIVEMQ_VERSION=5.18.3
ENV ACTIVEMQ_HOME=/opt/activemq

# ติดตั้ง ActiveMQ
RUN apt-get update && apt-get install -y wget \
    && wget https://archive.apache.org/dist/activemq/${ACTIVEMQ_VERSION}/apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz \
    && tar -xzf apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz -C /opt \
    && mv /opt/apache-activemq-${ACTIVEMQ_VERSION} ${ACTIVEMQ_HOME} \
    && rm apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz

# กำหนด Working Directory
WORKDIR ${ACTIVEMQ_HOME}

# เปิดพอร์ต
EXPOSE 61616 8161

# เริ่มต้น ActiveMQ
CMD ["/opt/activemq/bin/activemq", "console"]