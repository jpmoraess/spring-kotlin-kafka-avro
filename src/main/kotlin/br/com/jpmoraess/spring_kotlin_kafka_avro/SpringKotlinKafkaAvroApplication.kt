package br.com.jpmoraess.spring_kotlin_kafka_avro

import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaConfigProperties
import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaConsumerConfigProperties
import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.config.properties.KafkaProducerConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    value = [
        KafkaConfigProperties::class,
        KafkaConsumerConfigProperties::class,
        KafkaProducerConfigProperties::class
    ]
)
class SpringKotlinKafkaAvroApplication

fun main(args: Array<String>) {
    runApplication<SpringKotlinKafkaAvroApplication>(*args)
}
