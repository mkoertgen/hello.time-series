#!/usr/bin/env python3
from datetime import datetime, timezone
import os
import pulsar
from pulsar.schema import AvroSchema
from condition import Condition
from settings import PULSAR_HOST, PULSAR_TOPIC

PULSAR_SUBSCRIPTION_NAME = os.getenv(
    'PULSAR_SUBSCRIPTION_NAME', 'my-python-sub')
client = pulsar.Client(f'pulsar://{PULSAR_HOST}:6650')
consumer = client.subscribe(
    topic=PULSAR_TOPIC, subscription_name=PULSAR_SUBSCRIPTION_NAME, schema=AvroSchema(Condition))


def receive():
    while True:
        msg = consumer.receive()
        model = msg.value()
        try:
            ts = datetime.fromtimestamp(
                msg.publish_timestamp() / 1000.0, timezone.utc)
            id = msg.message_id()
            t = model.temperature
            h = model.humidity
            print(
                f'Received msg(time={ts} id={id}), condition(T={t:.2f} H={h:.2f})')
            # Acknowledge successful processing of the message
            consumer.acknowledge(msg)
        except:
            # Message failed to be processed
            consumer.negative_acknowledge(msg)


if __name__ == '__main__':
    try:
        receive()
    finally:
        client.close()
