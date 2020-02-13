# Redash

**NOTE:** At this time we discourage Redash because

- it is based on a rather old technology (**Python 2.7**, Flask, SQlAlchemy)
- the entry barrier w.r.t. containerization is too high, e.g. 
  - no auto-setup for PostGres,
  - no graceful container shutdown
  - ...
  - and in general no good experience.

## Usage

Start the stack using

```console
cd redash
docker-compose up -d
```

Next, check your Redash UI [localhost:3030](http://localhost:3030)

![redash-ui](img/redash/redash-ui.jpg)

## References

- [redash.io](https://redash.io/)
- [Supported Data sources](https://redash.io/help/data-sources/setup/supported-data-sources)
- [Setting up a Redash Instance # Docker](https://redash.io/help/open-source/setup#docker)
- [getredash/.../docker-compose.yml](https://github.com/getredash/setup/blob/master/data/docker-compose.yml)