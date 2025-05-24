package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config

import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaConfigProperties
import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaProducerConfigProperties
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.io.Serializable


@Configuration
class KafkaProducerConfig<K : Serializable, V : SpecificRecordBase>(
    private val kafkaConfigProperties: KafkaConfigProperties,
    private val kafkaProducerConfigProperties: KafkaProducerConfigProperties
) {

    @Bean
    fun producerConfig(): Map<String, Any> {
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigProperties.bootstrapServers,
            kafkaConfigProperties.schemaRegistryUrlKey to kafkaConfigProperties.schemaRegistryUrl,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigProperties.keySerializerClass,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigProperties.valueSerializerClass,
            ProducerConfig.BATCH_SIZE_CONFIG to (
                    kafkaProducerConfigProperties.batchSize * kafkaProducerConfigProperties.batchSizeBoostFactor
                    ),
            ProducerConfig.LINGER_MS_CONFIG to kafkaProducerConfigProperties.lingerMs,
            ProducerConfig.COMPRESSION_TYPE_CONFIG to kafkaProducerConfigProperties.compressionType,
            ProducerConfig.ACKS_CONFIG to kafkaProducerConfigProperties.acks,
            ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to kafkaProducerConfigProperties.requestTimeoutMs,
            ProducerConfig.RETRIES_CONFIG to kafkaProducerConfigProperties.retryCount
        )
    }

    @Bean
    fun producerFactory(): ProducerFactory<K, V> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> {
        return KafkaTemplate(producerFactory())
    }
}
