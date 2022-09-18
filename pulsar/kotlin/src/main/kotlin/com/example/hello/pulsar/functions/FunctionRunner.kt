package com.example.hello.pulsar.functions

import com.example.hello.pulsar.services.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.functions.api.Context
import org.slf4j.LoggerFactory

class FunctionRunner<I, O>(
  private val source: Consumer<I>,
  private val func: org.apache.pulsar.functions.api.Function<I, O>,
  private val context: Context,
  private val sink: Producer<O>
) : PulsarRunner(), FunctionRunnable {
  override fun callOnce() {
    val messages = source.batchReceive()
    if (messages.size() == 0) return
    log.debug("Received batch (size={})", messages.size())
    messages.forEach {
      val output = func.process(it.value, context)
      sink.send(output)
    }
    source.acknowledge(messages)
    log.debug("Processed batch (size={})", messages.size())
  }

  override fun close() {
    stop()
    source.close()
    sink.close()
  }

  companion object {
    private val log = LoggerFactory.getLogger(FunctionRunner::class.java)
  }
}
