FROM vromero/activemq-artemis

ENV ARTEMIS_USERNAME=admin
ENV ARTEMIS_PASSWORD=admin

VOLUME ["/var/lib/artemis"]

EXPOSE 61616 8161