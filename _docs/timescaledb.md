# TimeScaleDB

PoC on TimeScaleDb

## Setup

Setup a timescaledb instance

```bash
$docker-compose -f docker-compose.timescaledb.yml run --rm timescaledb-cli
Starting hellotick-stack_timescaledb_1 ... done

# Connect
$psql -U postgres -h timescaledb
Password for user postgres: *******
psql (11.5)
Type "help" for help.

# Create database
CREATE database tutorial;
\c tutorial

# Create (hyper) table
CREATE TABLE conditions (
    time        TIMESTAMPTZ         NOT NULL,
    location    text                NOT NULL,
    temperature DOUBLE PRECISION    NULL
);
SELECT create_hypertable('conditions', 'time');

# Insert & query
INSERT INTO conditions(time, location, temperature, humidity)
  VALUES (NOW(), 'office', 70.0, 50.0);
SELECT * FROM conditions ORDER BY time DESC LIMIT 100;
```

## Administration

You may use the PostgreSQL Web UI [sosedoff/pgweb](https://github.com/sosedoff/pgweb), i.e.

```console
docker-compose up -d pgweb
```

And check [localhost:5433](http://localhost:5433)

![pgweb-1](img/timescaledb/pgweb-1.jpg)

![pgweb-2](img/timescaledb/pgweb-2.jpg)

## Data Import / Migration

### CSV Import

Download CSV sample data from

- [Sample Datasets](https://docs.timescale.com/latest/tutorials/other-sample-datasets)

and upload posting to `/conditions/upload`, e.g.

```console
$curl -X POST http://localhost:5000/conditions/upload --data-binary @weather_small_conditions.csv
1000000
```

See also

- [Inserting data into the hypertable](https://docs.timescale.com/latest/getting-started/migrating-data#csv-import)
- [timescale/timescaledb-parallel-copy](https://github.com/timescale/timescaledb-parallel-copy)

### From InfluxDB

- [Migration from InfluxDB to TimescaleDB using Outflux](https://docs.timescale.com/latest/getting-started/migrating-data#outflux)

## References

- [blog.timescale.com](https://blog.timescale.com/blog/)
- [timescale/timescaledb](https://github.com/timescale/timescaledb)
- [timescale/tsbs](https://github.com/timescale/tsbs) - Time Series Benchmark Suite, a tool for comparing and evaluating databases for time series data
- [TimescaleDB vs. InfluxDB: Purpose built differently for time-series data](https://blog.timescale.com/blog/timescaledb-vs-influxdb-for-time-series-data-timescale-influx-sql-nosql-36489299877/)
- [Building a scalable time-series database using Postgres](https://www.percona.com/live/17/sites/default/files/slides/timescale-percona-Apr-2017.pdf)
- [Benchmarking TimescaleDB vs. InfluxDB for Time-Series Data](https://www.outfluxdata.com/assets/Timescale_WhitePaper_Benchmarking_Influx.pdf)
- [Time-series data: Why (and how) to use a relational database instead of NoSQL](https://blog.timescale.com/blog/time-series-data-why-and-how-to-use-a-relational-database-instead-of-nosql-d0cd6975e87c/)

### .NET

- [Entity Framework Core / Npgsql](https://www.npgsql.org/efcore/)
- [ASP.NET Core, Entity Framework Core with PostgreSQL Code First](https://medium.com/faun/asp-net-core-entity-framework-core-with-postgresql-code-first-d99b909796d7)
- [Timeseries Databases Part 3: Writing Data to TimescaleDB from .NET](https://bytefish.de/blog/timeseries_databases_3_timescaledb/)
- [bytefish/GermanWeatherDataExample](https://github.com/bytefish/GermanWeatherDataExample)
