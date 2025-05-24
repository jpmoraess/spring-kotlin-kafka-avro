package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.producer

import org.apache.avro.specific.SpecificRecordBase
import org.springframework.kafka.support.SendResult
import java.io.Serializable
import java.util.function.BiConsumer

interface KafkaProducer<K : Serializable, V : SpecificRecordBase> {

    /**
     * Sends a message to the specified Kafka topic with the given key and value.
     *
     * @param topic The Kafka topic to which the message will be sent.
     * @param key The key associated with the message.
     * @param value The value of the message, which must be a SpecificRecordBase.
     * @param callback A callback function that will be called upon completion of the send operation.
     */
    fun send(topic: String, key: K, value: V, callback: BiConsumer<SendResult<K, V>, Throwable>)
}
