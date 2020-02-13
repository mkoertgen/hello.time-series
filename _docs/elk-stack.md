# ELK stack

## Usage

Start the stack using

```console
cd elk
docker-compose up -d
```

Next, check Kibana at [localhost:5601](http://localhost:5601)

![kibana](img/elk/kibana.jpg)

## References

- [What is the ELK Stack?](https://www.elastic.co/what-is/elk-stack)
- [Elasticsearch as a Time Series Data Store](https://www.elastic.co/blog/elasticsearch-as-a-time-series-data-store)

## Import CSV

### Logstash

Most straight forward way is to import CSV via logstash, i.e.

1. Download Weather [Sample Datasets](https://docs.timescale.com/latest/tutorials/other-sample-datasets)
2. Put `weather_small_conditions.csv` into `./logstash/data/csv`
3. Startup logstash (`docker-compose up -d logstash`)
4. Import Kibana dashboards (`./kibana/export_weather.ndjson`)

### ELK File Data Visualizer

You will need to split large CSV into chunks of `< 100MB`, e.g. using [split](https://ss64.com/bash/split.html)

```bash
split -l 100000 source.csv dest -d --additional-suffix=.csv
```

Next, upload a chunk to [Kibana / Machine Learning / Data Visualizer](http://localhost:5601/app/ml#/filedatavisualizer?_g=())

See

- [Importing CSV and Log Data into Elasticsearch with File Data Visualizer](https://www.elastic.co/blog/importing-csv-and-log-data-into-elasticsearch-with-file-data-visualizer)
- Stack Overflow: [Batch file to split .csv file](https://stackoverflow.com/a/23696118/2592915)

### Converting CSV to ndjson

Using

- [julien-f/csv2json](https://github.com/julien-f/csv2json) and
- [jq](https://stedolan.github.io/jq/)

to convert CSV data to [new-line delimited json (ndjson)](http://ndjson.org/) which is easy to digest for filebeat, cf.: [filebeat-input-log-config-json](https://www.elastic.co/guide/en/beats/filebeat/master/filebeat-input-log.html#filebeat-input-log-config-json)

```console
npx csv2json -d input.csv output.json
jq -c ".[] | del(.unwantedKey) | .someTimestamp |= " input.json > output.ndjson
```
