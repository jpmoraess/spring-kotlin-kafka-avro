package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config

import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaConfigProperties
import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaConsumerConfigProperties
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.io.Serializable

@Configuration
class KafkaConsumerConfig<K : Serializable, V : SpecificRecordBase>(
    private val kafkaConfigProperties: KafkaConfigProperties,
    private val kafkaConsumerConfigProperties: KafkaConsumerConfigProperties
) {
    @Bean
    fun consumerConfigs(): Map<String, Any> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigProperties.bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to kafkaConsumerConfigProperties.keyDeserializer,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to kafkaConsumerConfigProperties.valueDeserializer,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaConsumerConfigProperties.autoOffsetReset,
            kafkaConfigProperties.schemaRegistryUrlKey to kafkaConfigProperties.schemaRegistryUrl,
            kafkaConsumerConfigProperties.specificAvroReaderKey to kafkaConsumerConfigProperties.specificAvroReader,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to kafkaConsumerConfigProperties.sessionTimeoutMs,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to kafkaConsumerConfigProperties.heartbeatIntervalMs,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to kafkaConsumerConfigProperties.maxPollIntervalMs,
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG to (
                    kafkaConsumerConfigProperties.maxPartitionFetchBytesDefault *
                            kafkaConsumerConfigProperties.maxPartitionFetchBytesBoostFactor
                    ),
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to kafkaConsumerConfigProperties.maxPollRecords
        )
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<K, V> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> {
        return ConcurrentKafkaListenerContainerFactory<K, V>().apply {
            consumerFactory = consumerFactory()
            isBatchListener = kafkaConsumerConfigProperties.batchListener
            setConcurrency(kafkaConsumerConfigProperties.concurrencyLevel)
            setAutoStartup(kafkaConsumerConfigProperties.autoStartup)
            containerProperties.pollTimeout = kafkaConsumerConfigProperties.pollTimeoutMs
        }
    }
}
