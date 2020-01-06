// adapted from https://github.com/tobymurray/mqtt-to-timescaledb
require('dotenv').config()

const Influx = require('influx')

const config = {
  mqtt: {
    url: `mqtt://${process.env.MQTT_HOST}`,
    options: {
      username: process.env.MQTT_USERNAME,
      password: process.env.MQTT_PASSWORD
    },
    topic: process.env.MQTT_TOPIC
  },
  // influx connecting, cf.:
  // - https://github.com/node-influx/node-influx
  influx: {
    host: process.env.INFLUX_HOST || 'localhost',
    port: process.env.INFLUX_PORT || 8086,
    database: process.env.INFLUX_DATABASE || 'weather_db',
    schema: [{
      measurement: 'conditions',
      fields: {
        temperature: Influx.FieldType.FLOAT,
        humidity: Influx.FieldType.FLOAT,
        lon: Influx.FieldType.FLOAT,
        lat: Influx.FieldType.FLOAT
      },
      tags: [
        'device_id'
      ]
    }]
  }
};
console.debug(config);

const mqtt = require('mqtt');
const mqttClient = mqtt.connect(config.mqtt.url, config.mqtt.options);

const influx = new Influx.InfluxDB(config.influx);

mqttClient.on('message', (topic, message) => {
  console.debug(`${topic}: ${message}`);
  // TODO: buffering
  let condition = JSON.parse(message.toString());
  insertCondition(condition);
});

mqttClient.on('connect', () => {
  console.debug("Connected!");
  mqttClient.subscribe(`${process.env.MQTT_TOPIC}`);
});

const insertCondition = (condition) => {
  influx.writePoints([{
    measurement: 'conditions',
    timestamp: new Date(condition.time),
    tags: {
      device_id: condition.id
    },
    fields: {
      temperature: condition.temperature,
      humidity: condition.humidity,
      lon: condition.lon,
      lat: condition.lat
    }
  }]).catch(e => {
    console.error(e.stack);
    //throw e
  });
};
