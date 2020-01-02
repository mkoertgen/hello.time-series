# ELK stack

## Usage

Start the stack using

```console
docker-compose -f docker-compose.elk.yml up -d
```

Next, check Kibana at [localhost:5601](http://localhost:5601)

![kibana](img/elk/kibana.jpg)

## References

- [What is the ELK Stack?](https://www.elastic.co/what-is/elk-stack)
- [Elasticsearch as a Time Series Data Store](https://www.elastic.co/blog/elasticsearch-as-a-time-series-data-store)

## Converting CSV to ndjson

Using

- [julien-f/csv2json](https://github.com/julien-f/csv2json) and
- [jq](https://stedolan.github.io/jq/)

to convert CSV data to [new-line delimited json (ndjson)](http://ndjson.org/) which is easy to digest for filebeat, cf.: [filebeat-input-log-config-json](https://www.elastic.co/guide/en/beats/filebeat/master/filebeat-input-log.html#filebeat-input-log-config-json)

```console
npx csv2json -d input.csv output.json
jq -c ".[] | del(.unwantedKey) | .someTimestamp |= " input.json > output.ndjson
```
