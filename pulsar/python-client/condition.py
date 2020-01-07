from pulsar.schema import Record, String, Float


class Condition(Record):
    time = String()
    id = String()
    temperature = Float()
    humidity = Float()
