package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
data class KafkaConsumerConfigProperties(
    val keyDeserializer: String,
    val valueDeserializer: String,
    val autoOffsetReset: String,
    val specificAvroReader: String,
    val specificAvroReaderKey: String,
    val batchListener: Boolean,
    val autoStartup: Boolean,
    val concurrencyLevel: Int,
    val sessionTimeoutMs: Int,
    val heartbeatIntervalMs: Int,
    val maxPollIntervalMs: Int,
    val pollTimeoutMs: Long,
    val maxPollRecords: Int,
    val maxPartitionFetchBytesDefault: Int,
    val maxPartitionFetchBytesBoostFactor: Int,
)
