package com.examples.hello.pulsar.producers;

import com.examples.hello.pulsar.producers.batch.BatchProducer;
import com.examples.hello.pulsar.services.PulsarRunner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.pulsar.client.api.Producer;

@Slf4j
@RequiredArgsConstructor
public class BatchProducerRunner<T> extends PulsarRunner implements ProducerRunnable {
	private final Producer<T> producer;
	private final BatchProducer<T> source;

	@SneakyThrows
	@Override
	protected void callOnce() {
		val messages = source.call();
		for (val message : messages)
			producer.send(message);
		log.debug("Produced {} values (batch)", messages.size());
	}

	@Override
	public void close() throws Exception {
		producer.close();
	}
}
