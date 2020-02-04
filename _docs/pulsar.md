# Pulsar

## Usage

Start the stack using

```console
cd pulsar
docker-compose up -d
```

Next, check `python-consumer` is receiving messages (in AvroSchema) from `python-producer`

```console
$docker-compose logs -f python-consumer
...
python-consumer_1  | Received condition: time=2020-01-07T21:49:19.089079 id=sensor-2 T=31.87 H=72.50
python-consumer_1  | Received condition: time=2020-01-07T21:49:19.091122 id=sensor-1 T=10.40 H=34.57
python-consumer_1  | Received condition: time=2020-01-07T21:49:19.093253 id=sensor-2 T=5.75 H=24.36
...
```

Same for the `java-consumer`.

Finally, check the topic stats via the Pulsar Admin REST Api at [admin/v2/persistent/public/default/conditions/stats](http://localhost:8080/admin/v2/persistent/public/default/conditions/stats)

```json
GET http://localhost:8080/admin/v2/persistent/public/default/conditions/stats
{
  "msgRateIn": 368.3959382487755,
  "msgThroughputIn": 31002.792438576224,
  "msgRateOut": 368.4694163923984,
  "msgThroughputOut": 31008.896610525502,
  "averageMsgSize": 84.15617334423548,
  "storageSize": 2424263,
  "backlogSize": 12287,
  ...
```

The schema (here Avro)

```json
GET http://localhost:8080/admin/v2/schemas/public/default/conditions/schema
{
  "version": 0,
  "type": "AVRO",
  "timestamp": 0,
  "data": "{\n \"name\": \"Condition\",\n \"type\": \"record\",\n \"fields\": [\n  {\n   \"name\": \"humidity\",\n   \"type\": [\n    \"null\",\n    \"float\"\n   ]\n  },\n  {\n   \"name\": \"temperature\",\n   \"type\": [\n    \"null\",\n    \"float\"\n   ]\n  }\n ]\n}",
  "properties": {
  }
}
```

## Notes

### Python

Since [pulsar-client](https://pypi.org/project/pulsar-client/) uses [manylinux](https://github.com/pypa/manylinux) for native dependencies (e.g. [fastavro](https://pypi.org/project/fastavro/)) you can use the python client only on linux compatible to glibc2.5. This rules out Windows, Mac, and [Alpine Linux](https://alpinelinux.org/) ([musl-libc](https://www.musl-libc.org/)).

As of now, the most recent Python to use is 3.7. You may check at [pulsar-client/#files](https://pypi.org/project/pulsar-client/#files) for updates.

So basically, we are using [python:3.7.6-slim-buster](https://hub.docker.com/layers/python/library/python/3.7.6-slim-buster/images/sha256-47cabc28176273541f261c4efd2a5d4d02262025f05ca0ed5df3680552f1c1bb) as a base image.

### Java

tbd

#### Links

- [The Pulsar Java client](https://pulsar.apache.org/docs/en/client-libraries-java/)
- [Get started with Pulsar Functions](https://pulsar.apache.org/docs/en/functions-quickstart/)

## References

- [pulsar.apache.org](https://pulsar.apache.org/)
- [apache/pulsar](https://github.com/apache/pulsar)
- [Run Pulsar in Docker](https://pulsar.apache.org/docs/en/standalone-docker/)

### Web UI

- [apache/pulsar-manager](https://github.com/apache/pulsar-manager)
- [How to use Apache Pulsar Manager with HerdDB](https://medium.com/streamnative/how-to-use-apache-pulsar-manager-with-herddb-dd265c955ca4)

### Deployment

- [Deploying Pulsar on Kubernetes](https://pulsar.apache.org/docs/en/deploy-kubernetes/)
- [How to Deploy Apache Pulsar Cluster in Kubernetes](https://www.syscrest.com/2019/09/installing-pulsar-on-kubernetes-using-helm/)
