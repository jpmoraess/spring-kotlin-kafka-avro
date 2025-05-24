package br.com.jpmoraess.spring_kotlin_kafka_avro.adapter.kafka

import br.com.jpmoraess.spring_kotlin_kafka_avro.User
import br.com.jpmoraess.spring_kotlin_kafka_avro.application.ports.input.CreateUserListener
import br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CreateUserKafkaListener(
    private val createUserListener: CreateUserListener
) : KafkaConsumer<User> {

    @KafkaListener(
        id = "\${kafka-consumer-config.consumer-group-id}",
        topics = ["\${app-service.user-created-topic-name}"],
    )
    override fun receive(
        @Payload messages: List<User>,
        @Header(value = KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(value = KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(value = KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        log.info(
            "{} number of users created received with keys:{}, partitions:{} and offsets: {}",
            messages.size,
            keys.toString(),
            partitions.toString(),
            offsets.toString()
        )
        messages.forEach {
            try {
                createUserListener.userCreated(user = it.toString())
            } catch (e: Exception) {
                TODO("Not yet implemented")
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(CreateUserKafkaListener::class.java)
    }
}
