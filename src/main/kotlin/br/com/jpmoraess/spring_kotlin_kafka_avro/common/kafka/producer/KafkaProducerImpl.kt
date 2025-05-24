package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.producer

import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.producer.exception.KafkaProducerException
import jakarta.annotation.PreDestroy
import org.apache.avro.specific.SpecificRecordBase
import org.slf4j.LoggerFactory
import org.springframework.kafka.KafkaException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

@Component
class KafkaProducerImpl<K : Serializable, V : SpecificRecordBase>(
    private val kafkaTemplate: KafkaTemplate<K, V>
) : KafkaProducer<K, V> {

    override fun send(
        topic: String,
        key: K,
        value: V,
        callback: BiConsumer<SendResult<K, V>, Throwable>
    ) {
        log.info("Sending message to topic: $topic with key: $key and value: $value")
        try {
            val kafkaResultFuture: CompletableFuture<SendResult<K, V>> = kafkaTemplate.send(topic, key, value)
            kafkaResultFuture.whenComplete(callback)
        } catch (e: KafkaException) {
            log.error("Error on kafka producer with key: $key, message: $value and exception: ${e.message}", e)
            throw KafkaProducerException("Error on kafka producer with key: $key and message: $value")
        }
    }

    @PreDestroy
    fun close() {
        log.info("Closing Kafka producer")
        try {
            kafkaTemplate.flush()
            kafkaTemplate.destroy()
        } catch (e: Exception) {
            log.error("Error closing Kafka producer: ${e.message}", e)
            throw KafkaProducerException("Error closing Kafka producer", e)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KafkaProducerImpl::class.java)
    }
}
