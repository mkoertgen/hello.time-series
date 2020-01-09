from pulsar import Function


class AverageConditionFunction(Function):
    def __init__(self):
        self.conditions_topic = "persistent://public/default/conditions"

    def process(self, item, context):
        if self.is_fruit(item):
            context.publish(self.fruits_topic, item)
        elif self.is_vegetable(item):
            context.publish(self.vegetables_topic, item)
        else:
            warning = "The item {0} is neither a fruit nor a vegetable".format(
                item)
            context.get_logger().warn(warning)
