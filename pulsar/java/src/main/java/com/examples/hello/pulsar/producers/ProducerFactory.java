package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.producers.batch.ConditionBatchProducer;
import com.examples.hello.pulsar.services.PulsarFactory;
import com.examples.hello.pulsar.weather.conditions.Condition;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Factory
@Slf4j
public class ProducerFactory {
	public static final String BATCH_CONDITIONS = "batch-conditions";
	private final Random rnd = new Random();
	@Value("${pulsar.producer.name}")
	String name;
	@Inject
	PulsarFactory factory;
	@Value("${pulsar.producer.topic}")
	String topic;
	@Value("${pulsar.producer.interval}")
	Duration interval;
	@Value("${pulsar.producer.batch}")
	int batchSize;

	@Singleton
	@Named("conditions")
	public ProducerRunnable producer() {
		val producer = factory.producerOf(name, topic, Condition.class);
		return new ProducerRunner<>(producer, singleCondition());
	}

	@Singleton
	@Named(BATCH_CONDITIONS)
	public ProducerRunnable batchProducer() {
		val producer = factory.producerOf(name, topic, Condition.class);
		return new BatchProducerRunner<>(producer, batchConditions());
	}

	private ConditionProducer singleCondition() {
		return () -> {
			if (interval.toMillis() > 0) Thread.sleep(interval.toMillis());
			return randomCondition();
		};
	}

	private ConditionBatchProducer batchConditions() {
		return () -> Stream.generate(this::randomCondition).limit(batchSize).collect(Collectors.toList());
	}

	private Condition randomCondition() {
		return Condition.builder().temperature(rnd.nextFloat() * 40).humidity(rnd.nextFloat() * 100).build();
	}
}
