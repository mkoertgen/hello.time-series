# hello.tick-stack

Checking out the [TICK-Stack](https://www.influxdata.com/time-series-platform/), i.e.

- [Telegraf](https://www.influxdata.com/time-series-platform/telegraf/)
- [InfluxDB](https://hub.docker.com/_/influxdb)
- [Chronograf](https://www.influxdata.com/time-series-platform/chronograf/)
- [Kapacitor](https://www.influxdata.com/time-series-platform/kapacitor/)
- and additionally [Grafana](https://grafana.com/)

![TICK-Stack](https://lh4.googleusercontent.com/kB1k79-NiJgt0bi0P-7n1n2HUUDITEMxeyS8lKHmgvpUZ_c0xzLeUBgvp91JTM_wWpJ3VjZAHlE-PbivjBqVZeZJNQbtQPtKCgmfD3AVgcGhya1cFoFHZgvYCQezxv4uBcWGOGxU)

## Usage

```console
docker-compose up -d chronograf
```

Then, visit

- Chronograf at [http://localhost:8888](http://localhost:8888)
- Grafana at [http://localhost:3000](http://localhost:3000) (default login: `admin/123admin`)

How to proceed? Check the documentation at [_docs/index](../_docs/index).

## Links

- [Managing the TICK Stack with Docker App](https://www.docker.com/blog/managing-tick-stack-with-docker-app/)
