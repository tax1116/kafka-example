package kr.co.taek.dev.parallel.consumer.example.adapter.inbound.kafka

import io.confluent.parallelconsumer.ParallelConsumerOptions
import io.confluent.parallelconsumer.ParallelStreamProcessor
import io.confluent.parallelconsumer.internal.DrainingCloseable
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PreDestroy
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Duration

private val log = KotlinLogging.logger {}

@Component
class KafkaParallelConsumer(
    @Value("\${kafka.bootstrap-address}") bootstrapAddress: String,
    @Value("\${kafka.consumer.group-id}") groupId: String,
    @Value("\${kafka.consumer.topic}") topic: String,
) {
    private val props =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
        )

    private val kafkaConsumer = KafkaConsumer<String, String>(props)

    // NOTE: batchSize 가 1보다 크면 batch 모드. default batchSize 는 1
    // NOTE: default commitMode 는 PERIODIC_CONSUMER_ASYNCHRONOUS
    private val parallelConsumerOptions =
        ParallelConsumerOptions.builder<String, String>()
            .ordering(ParallelConsumerOptions.ProcessingOrder.UNORDERED) // NOTE: UNOREDRED 일 때, 병렬로 처리됨.
            .consumer(kafkaConsumer)
            .build()

    private val parallelConsumer =
        ParallelStreamProcessor.createEosStreamProcessor(parallelConsumerOptions)
            .also { it.subscribe(listOf(topic)) }

    @EventListener(ApplicationStartedEvent::class)
    fun consume() {
        parallelConsumer.poll { context ->
            val record = context.singleRecord.consumerRecord
            handle(record)
        }
    }

    @PreDestroy
    fun close() {
        parallelConsumer.close(Duration.ofSeconds(30), DrainingCloseable.DrainingMode.DRAIN)
    }

    private fun handle(record: ConsumerRecord<String, String>) {
        val startTime: Long = System.currentTimeMillis()
        Thread.sleep(3000)
        val endTime: Long = System.currentTimeMillis()

        log.info { "Received message - topic: ${record.topic()}, partition: ${record.partition()}, offset: ${record.offset()}, key: ${record.key()}, value: ${record.value()}, Elapsed time: ${endTime - startTime}ms" }
    }
}
