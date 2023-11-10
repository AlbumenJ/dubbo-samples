# Dubbo Triple With Protobuf

This example shows the basic usage of Triple protocol with a typical request-response model demo that uses IDL as the method of defining Dubbo service.

## Start Server

**NOTE: This step has been automatically done if you are in GitHub CodeSpace**

```shell
bash 1-basic/dubbo-samples-triple-idl/launch-server.sh
```

What this script does is to start a triple server with a simple service `org.apache.dubbo.samples.tri.unary.Greeter` defined in `src/main/proto/greeter.proto`.

## Start Client

There are two ways to test the server works as expected:
* Standard HTTP tools like cURL.
* Dubbo sdk client.

### cURL in Terminal

```shell
curl \
    --header "Content-Type: application/json" \
    --data '{"name": "Dubbo From cURL"}' \
    http://localhost:50052/org.apache.dubbo.samples.tri.unary.Greeter/greet/
```

And you will see the response:
    
```json
{
  "message": "hello,Dubbo From cURL"
}
```

### cURL in your local machine

```shell
curl \
    --header "Content-Type: application/json" \
    --data '{"name": "Dubbo From cURL"}' \
    https://<CodeSpace Endpoint>/org.apache.dubbo.samples.tri.unary.Greeter/greet/
```

And you will see the response:

```json
{
  "message": "hello,Dubbo From cURL"
}
```

### Start client

```shell
bash 1-basic/dubbo-samples-triple-idl/launch-client.sh
```

What this script does is to start a triple client with a simple service `org.apache.dubbo.samples.tri.unary.Greeter` defined in `src/main/proto/greeter.proto`.

And you will see the response:

```
hello,Dubbo From Client
```

