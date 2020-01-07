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

Finally, check the topic stats via the Pulsar Admin REST Api at [admin/v2/persistent/public/default/conditions/stats](http://localhost:8080/admin/v2/persistent/public/default/conditions/stats)

```json
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

## References

- [pulsar.apache.org](https://pulsar.apache.org/)
- [apache/pulsar](https://github.com/apache/pulsar)
- [Run Pulsar in Docker](https://pulsar.apache.org/docs/en/standalone-docker/)

### Web UI

- [apache/pulsar-manager](https://github.com/apache/pulsar-manager)
- [How to use Apache Pulsar Manager with HerdDB](https://medium.com/streamnative/how-to-use-apache-pulsar-manager-with-herddb-dd265c955ca4)
