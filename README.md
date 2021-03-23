# Organizer: A gRPC Service

## Installation

**1. Clone the following repository:**

* https://github.com/hu-tao-supremacy/organizer
  
* https://github.com/hu-tao-supremacy/api
  
* https://github.com/hu-tao-supremacy/migrations

**2. Start docker containers by executing the following on organizer repository:**

```
docker-compose -f docker-compose-local.yaml up -d
```

**3. Move to the migrations repository and execute the following:**

```
yarn
make migrate
make seed
```

**4. Move to the organizer repository and execute the following:**

```
make apis
mvn install
```

## Running

1. Move to the organizer repository and create environment variables using `export`

2. In the organizer repository and execute `mvn spring-boot:run`

3. Install [BloomRPC](https://github.com/uw-labs/bloomrpc) and open it.

  3.1 In the top left corner, import path of `.../organizer/apis/proto`

  3.2 Import protos of `.../organizer/apis/proto/hts/organizer/service.proto`

  3.3 Select a remote procedure call and unary call to your gRPC port (for example, `localhost:50051`). You should obtain the call result.
