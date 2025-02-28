# ใช้ภาพ ActiveMQ Artemis อย่างเป็นทางการ
FROM vromero/activemq-artemis

# กำหนด environment variables (สามารถแก้ไขได้)
ENV ARTEMIS_USERNAME=admin
ENV ARTEMIS_PASSWORD=admin

# กำหนดโฟลเดอร์เก็บข้อมูลเป็น volume
VOLUME ["/var/lib/artemis"]

# เปิดพอร์ตที่จำเป็น
EXPOSE 61616 8161