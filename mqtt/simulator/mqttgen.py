#!/usr/bin/env python3
"""a simple sensor data generator that sends to an MQTT broker via paho"""
from datetime import datetime, timezone
import json
import os
import random
import sys
import time

import paho.mqtt.client as mqtt


def generate(host, port, username, password, topic, sensors, interval_ms, verbose):
    """generate data and send it to an MQTT broker"""
    mqttc = mqtt.Client()
    if username:
        mqttc.username_pw_set(username, password)
    mqttc.connect(host, port)

    keys = list(sensors.keys())
    interval_secs = interval_ms / 1000.0

    while True:
        sensor_id = random.choice(keys)
        sensor = sensors[sensor_id]

        data = {'id': sensor_id, 'time': datetime.utcnow().isoformat()}

        for val_def in sensor.get('values', []):
            min_val, max_val = val_def.get("range", [0, 100])
            val = random.uniform(min_val, max_val)
            data[val_def['name']] = val

        for key in ["lat", "lon"]:
            value = sensor.get(key)
            if value is not None:
                data[key] = value

        payload = json.dumps(data)

        if verbose:
            print("%s: %s" % (topic, payload))

        mqttc.publish(topic, payload)
        time.sleep(interval_secs)


def get_config(config, key: str, default_value=None):
    env_key = f'MQTT_{key.upper()}'
    val = os.getenv(env_key, config.get(key, default_value))
    return val


def main(config_path):
    """main entry point, load and validate config and call generate"""
    try:
        with open(config_path) as handle:
            config = json.load(handle)
            mqtt_config = config.get("mqtt", {})
            misc_config = config.get("misc", {})
            sensors = config.get("sensors")

            interval_ms = int(get_config(misc_config, 'interval_ms', '500'))
            verbose = misc_config.get("verbose", False)

            if not sensors:
                print("no sensors specified in config, nothing to do")
                return

            host = get_config(mqtt_config, 'host', 'localhost')
            port = mqtt_config.get("port", 1883)
            username = get_config(mqtt_config, 'username')
            password = get_config(mqtt_config, 'password')
            topic = get_config(mqtt_config, 'topic', 'mqttgen')

            generate(host, port, username, password,
                     topic, sensors, interval_ms, verbose)
    except IOError as error:
        print("Error opening config file '%s'" % config_path, error)


if __name__ == '__main__':
    if len(sys.argv) == 2:
        main(sys.argv[1])
    else:
        print("usage %s config.json" % sys.argv[0])
