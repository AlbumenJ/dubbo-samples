# Dubbo Triple With Protobuf

This example shows the basic usage of Triple protocol with a typical request-response model demo that uses IDL as the method of defining Dubbo service.

# Hands on Lab

## Start Server

This step has been automatically done if you are in GitHub CodeSpace.

```shell
$ bash 1-basic/dubbo-samples-idl/launch-server.sh
```

![server-started](https://dubbo.apache.org/imgs/docs3-v2/java-sdk/quickstart/idl-directly-server-started.jpg)

## Start Client

There are two ways to test the server works as expected:
* Standard HTTP tools like cURL.
* Dubbo SDK client.

### cURL in Terminal

You can use cURL to send a request to the server:

```shell
$ curl \
    --header "Content-Type: application/json" \
    --data '{"name": "Dubbo From cURL"}' \
    http://localhost:50051/org.apache.dubbo.samples.tri.unary.Greeter/greet/
```

And you will see the response:

```json
{
  "message": "hello,Dubbo From cURL"
}
```

![curl-terminal](https://dubbo.apache.org/imgs/docs3-v2/java-sdk/quickstart/idl-directly-curl-terminal.jpg)

### cURL in your local machine

Also, you can even use cURL to send a request to this **remote** code space server with the endpoint provided by GitHub CodeSpace **where ever you are**:

```shell
$ curl \
    --header "Content-Type: application/json" \
    --data '{"name": "Dubbo From cURL"}' \
    https://silver-system-qr479qwpxx29666-50051.app.github.dev/org.apache.dubbo.samples.tri.unary.Greeter/greet/
```

Before you run this command, you need to set the 50051 port in the GitHub CodeSpace to be public accessible. You can do this like below:

![Publicise](https://dubbo.apache.org/imgs/docs3-v2/java-sdk/quickstart/idl-directly-github-codespace-public.png)

Later you will see the response in your local machine:

```json
{
  "message": "hello,Dubbo From cURL"
}
```

![curl-remote](https://dubbo.apache.org/imgs/docs3-v2/java-sdk/quickstart/idl-directly-curl-remote.jpg)


### Start client

Not only cURL, you can also use Dubbo SDK client to send a request to the server:

```shell
$ bash 1-basic/dubbo-samples-idl/launch-client.sh
```

What this script does is to start a triple client with a simple service `org.apache.dubbo.samples.tri.unary.Greeter` defined in `src/main/proto/greeter.proto`.

And you will see the response:

```
hello,Dubbo From Client
```

![sdk](https://dubbo.apache.org/imgs/docs3-v2/java-sdk/quickstart/idl-directly-sdk.jpg)

# How it works

## Pom file

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-samples-idl</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <dubbo.version>3.3.0-beta.2</dubbo.version>
        <protobuf-java.version>3.19.6</protobuf-java.version>
        <protoc.version>3.22.3</protoc.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>${protobuf-java.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java-util</artifactId>
            <version>${protobuf-java.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-maven-plugin</artifactId>
                <version>${dubbo.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

In this pom file, there are two things you need to pay attention to:
* Add dubbo, protobuf-java and protobuf-java-util dependencies.
* Use dubbo-maven-plugin to generate java code from proto file.

Note: `dubbo-maven-plugin` is a plugin based on `protobuf-maven-plugin`, so you can use `protobuf-maven-plugin` to generate java code from proto file as well. However, using `dubbo-maven-plugin` is more simple and convenient.

## Proto file

```protobuf
syntax = "proto3";

option java_multiple_files = true;

package org.apache.dubbo.samples.tri.unary;

message GreeterRequest {
  string name = 1;
}

message GreeterReply {
  string message = 1;
}

service Greeter{

  rpc greet(GreeterRequest) returns (GreeterReply);

}
```

In this proto file, we have defined a service `Greeter` with a method `greet` which takes a `GreeterRequest` and returns a `GreeterReply`.

## Implementation of service

```java
public class GreeterImpl extends DubboGreeterTriple.GreeterImplBase {
    @Override
    public GreeterReply greet(GreeterRequest request) {
        return GreeterReply.newBuilder()
                .setMessage("hello," + request.getName())
                .build();
    }
}
```

Please note that the `GreeterImpl` extends `DubboGreeterTriple.GreeterImplBase` which is generated by `dubbo-maven-plugin` from the proto file. You can generate it by `mvn compile` command and will find it in `target/generated-sources/protobuf/java/org/apache/dubbo/samples/tri/unary/DubboGreeterTriple.java`.

## Server

```java
public class TriUnaryServer {
    public static void main(String[] args) {
        ServiceConfig<Greeter> service = new ServiceConfig<>();
        service.setInterface(Greeter.class);
        service.setProtocol(new ProtocolConfig("tri"));
        service.setRef(new GreeterImpl());
        service.export();
        new CountDownLatch(1).await();
    }
}
```

In this server, we have:
* Set the protocol to be `tri`.
* Set the service interface to be `Greeter`.
* Set the service implementation to be `GreeterImpl`.
* Export the service.

## Client

```java
public class TriUnaryClient {
    public static void main(String[] args) {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterface(Greeter.class);
        ref.setUrl("tri://127.0.0.1:50051");
        Greeter greeter = ref.get();

        GreeterReply reply = greeter.greet(
                GreeterRequest.newBuilder()
                        .setName("world")
                        .build());
        System.out.println(" Client received " + reply);
    }
}
```

In this client, we have:
* Set the remote address to be `tri://127.0.0.1:50051`
* Get the service proxy by `ref.get()`.
* Call the service method `greet` with a `GreeterRequest` and get the `GreeterReply`.
