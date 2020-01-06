// adapted from https://github.com/tobymurray/mqtt-to-timescaledb
require('dotenv').config()

const config = {
  mqtt: {
    url: `mqtt://${process.env.MQTT_HOST}`,
    options: {
      username: process.env.MQTT_USERNAME,
      password: process.env.MQTT_PASSWORD
    },
    topic: process.env.MQTT_TOPIC
  },
  // pg connecting, cf.:
  // - https://node-postgres.com/features/connecting#Environment%20variables
  // - https://node-postgres.com/features/connecting#Programmatic
  pgsql: {
    host: process.env.PGHOST || 'localhost',
    port: process.env.PGPORT || 5432,
    database: process.env.PGDATABASE,
    user: process.env.PGUSER,
    password: process.env.PGPASSWORD
  }
};
console.debug(config);

const mqtt = require('mqtt');
const mqttClient = mqtt.connect(config.mqtt.url, config.mqtt.options);

const {
  Pool
} = require('pg');
const pool = new Pool(config.pgsql);

mqttClient.on('connect', () => {
  console.debug("Connected!");
  mqttClient.subscribe(`${process.env.MQTT_TOPIC}`);
});

mqttClient.on('message', async (topic, message) => {
  console.debug(`${topic}: ${message}`);
  // TODO: buffering
  let condition = JSON.parse(message.toString());
  await insertCondition(condition);
});

const insertCondition = async (condition) => {
  const client = await pool.connect()
  try {
    await client.query('BEGIN');
    const text = 'INSERT INTO conditions(time, device_id, temperature, humidity, location) VALUES ($1, $2, $3, $4, $5)';
    const values = [
      condition.time,
      condition.id,
      condition.temperature,
      condition.humidity,
      // NOTE: point-type is special '(lon,lat)', cf.: https://stackoverflow.com/a/41494767/2592915
      `(${condition.lon},${condition.lat})`
    ];
    await client.query(text, values);
    await client.query('COMMIT');
  } catch (e) {
    await client.query('ROLLBACK');
    console.error(e.stack);
    //throw e
  } finally {
    client.release()
  }
};
