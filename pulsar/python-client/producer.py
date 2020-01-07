from datetime import datetime
import json
import os
import random
import time

import pulsar
from pulsar.schema import AvroSchema
from condition import Condition

PULSAR_HOST = os.getenv('PULSAR_HOST', 'localhost')
PULSAR_TOPIC = os.getenv('PULSAR_TOPIC', 'my-topic')
PULSAR_INTERVAL_MS = int(os.getenv('PULSAR_INTERVAL_MS', '0'))

client = pulsar.Client('pulsar://{}:6650'.format(PULSAR_HOST))
producer = client.create_producer(
    topic=PULSAR_TOPIC, schema=AvroSchema(Condition))


def generate():
    keys = ['sensor-1', 'sensor-2']
    interval_secs = PULSAR_INTERVAL_MS / 1000.0

    while True:
        sensor_id = random.choice(keys)
        model = Condition(time=datetime.utcnow().isoformat(), id=sensor_id,
                          temperature=random.uniform(0, 40), humidity=random.uniform(0, 100))
        producer.send(model)
        if interval_secs > 0:
            time.sleep(interval_secs)


if __name__ == '__main__':
    try:
        generate()
    finally:
        client.close()
