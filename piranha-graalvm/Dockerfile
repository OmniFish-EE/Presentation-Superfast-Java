#
# Create a custom JLink runtime
#
FROM eclipse-temurin:17 AS builder
RUN cd /usr/local && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar xfvz apache-maven-3.8.4-bin.tar.gz && \
    rm apache-maven-3.8.4-bin.tar.gz
COPY . /root
RUN export PATH=$PATH:/usr/local/apache-maven-3.8.4/bin && \
    cd /root && \
    mvn --no-transfer-progress clean install

#
# Create a slim Debian image with the custom JLink runtime
#
FROM scratch
COPY --from=builder /root/target/jlink /usr/local/helloworld
EXPOSE 8080
CMD ["/usr/local/helloworld/bin/helloworld"]
