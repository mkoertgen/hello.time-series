#!/usr/bin/env python3
import os
import pulsar
from pulsar.schema import AvroSchema
from condition import Condition

PULSAR_HOST = os.getenv('PULSAR_HOST', 'localhost')
PULSAR_TOPIC = os.getenv('PULSAR_TOPIC', 'my-topic')
PULSAR_SUBSCRIPTION_NAME = os.getenv('PULSAR_SUBSCRIPTION_NAME', 'my-sub')
client = pulsar.Client(f'pulsar://{PULSAR_HOST}:6650')
consumer = client.subscribe(
    topic=PULSAR_TOPIC, subscription_name=PULSAR_SUBSCRIPTION_NAME, schema=AvroSchema(Condition))


def receive():
    while True:
        msg = consumer.receive()
        model = msg.value()
        try:
            print(
                f'Received condition: time={model.time} id={model.id} T={model.temperature:.2f} H={model.humidity:.2f}')
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
