from pulsar.schema import Record, Float


class Condition(Record):
    temperature = Float()
    humidity = Float()
