FROM ubuntu:24.04

RUN apt-get update && apt-get install -y \
    openssh-server \
    bash \
    && mkdir /var/run/sshd

RUN useradd -ms /bin/bash testuser && echo "testuser:password" | chpasswd

COPY entrypoint.sh /home/testuser/entrypoint.sh
RUN chmod +x /home/testuser/entrypoint.sh

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]
