package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.helper

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class KafkaHelper(
    private val objectMapper: ObjectMapper
) {

    /**
     * Converts a JSON string to an object of the specified type.
     *
     * @param content The JSON string to convert.
     * @param valueType The class of the type
     */
    fun <T> getEventPayload(content: String, valueType: Class<T>): T {
        return try {
            objectMapper.readValue(content, valueType)
        } catch (e: Exception) {
            log.error("Could not read {} object!", valueType.name, e)
            throw RuntimeException("Could not read ${valueType.name} object!", e)
        }
    }

    fun <T> getKafkaCallback(
        topic: String,
        model: T,
        modelName: String
    ): BiConsumer<SendResult<String, T>, Throwable> {
        return BiConsumer { result, ex ->
            log.error(
                "Error while sending {} with Message: {} to Topic: {}",
                modelName,
                model.toString(),
                topic,
                ex
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KafkaHelper::class.java)
    }
}