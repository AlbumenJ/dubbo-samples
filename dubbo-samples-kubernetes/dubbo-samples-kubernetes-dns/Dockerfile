FROM openjdk:8-jre
ADD ./target/dubbo-samples-kubernetes-dns-1.0-SNAPSHOT.jar /dubbo-samples-kubernetes-dns-1.0-SNAPSHOT.jar
ADD chain.crt /chain.crt
ADD private.key /private.key
ENTRYPOINT ["java", "-classpath", "/dubbo-samples-kubernetes-dns-1.0-SNAPSHOT.jar", "org.apache.dubbo.samples.ConsumerBootstrap"]
