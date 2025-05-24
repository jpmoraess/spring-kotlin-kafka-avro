package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigProperties(
    val bootstrapServers: String,
    val schemaRegistryUrl: String,
    val schemaRegistryUrlKey: String,
    val numOfPartitions: Int,
    val replicationFactor: Short,
)
