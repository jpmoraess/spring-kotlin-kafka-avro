package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.consumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaConsumer<T : SpecificRecordBase> {
    /**
     * This method is called when messages are received from Kafka.
     *
     * @param messages The list of messages received.
     * @param keys The list of keys corresponding to the messages.
     * @param partitions The list of partitions from which the messages were received.
     * @param offsets The list of offsets for the messages.
     */
    fun receive(messages: List<T>, keys: List<String>, partitions: List<Int>, offsets: List<Long>)
}
