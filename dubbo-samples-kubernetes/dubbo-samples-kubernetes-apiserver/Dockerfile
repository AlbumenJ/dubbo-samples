FROM openjdk:8-jre
ADD ./target/dubbo-samples-kubernetes-apiserver-1.0-SNAPSHOT.jar /dubbo-samples-kubernetes-apiserver-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=20660", "-classpath", "/dubbo-samples-kubernetes-apiserver-1.0-SNAPSHOT.jar", "org.apache.dubbo.samples.ConsumerBootstrap"]
