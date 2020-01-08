#!/usr/bin/env python3
from datetime import datetime
import json
import os
import random
import time

import pulsar
from pulsar.schema import AvroSchema
from condition import Condition
from settings import PULSAR_HOST, PULSAR_TOPIC

PULSAR_INTERVAL_MS = int(os.getenv('PULSAR_INTERVAL_MS', '0'))
client = pulsar.Client(f'pulsar://{PULSAR_HOST}:6650')
producer = client.create_producer(
    topic=PULSAR_TOPIC, schema=AvroSchema(Condition))


def generate():
    keys = ['sensor-1', 'sensor-2']
    interval_secs = PULSAR_INTERVAL_MS / 1000.0

    while True:
        sensor_id = random.choice(keys)
        model = Condition(
            temperature=random.uniform(0, 40),
            humidity=random.uniform(0, 100)
        )
        producer.send(model)
        if interval_secs > 0:
            time.sleep(interval_secs)


if __name__ == '__main__':
    try:
        generate()
    finally:
        client.close()
