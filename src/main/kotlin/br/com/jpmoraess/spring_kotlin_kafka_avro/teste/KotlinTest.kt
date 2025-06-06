package br.com.jpmoraess.spring_kotlin_kafka_avro.teste

import org.apache.avro.specific.SpecificRecordBase
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.io.Serializable
import java.util.function.BiConsumer

// Minha interface para enviar mensagens para o Kafka
interface KafkaProducer<K : Serializable, V : SpecificRecordBase> {
    fun send(topic: String, key: K, value: V, callback: BiConsumer<SendResult<K, V>, Throwable?>)
}

// Implementação da interface KafkaProducer
class KafkaProducerImpl<K : Serializable, V : SpecificRecordBase>(
    private val kafkaTemplate: KafkaTemplate<K, V>
) : KafkaProducer<K, V> {

    override fun send(
        topic: String,
        key: K,
        value: V,
        callback: BiConsumer<SendResult<K, V>, Throwable?>
    ) {
        try {
            val sendResult = kafkaTemplate.send(topic, key, value)
            sendResult.whenComplete(callback)
        } catch (e: Exception) {
            throw RuntimeException("Failed to send message to Kafka topic: $topic", e)
        }
    }
}

// Exemplo de uso
class MyPublisher(
    private val kafkaProducer: KafkaProducer<String, SpecificRecordBase>
) {
    fun publish(topic: String, key: String, value: SpecificRecordBase) {
        kafkaProducer.send(topic, key, value) { result, exception ->
            if (exception != null) {
                println("Failed to send message: ${exception.message}")
            } else {
                println("Message sent successfully with offset: ${result?.recordMetadata?.offset()}")
            }
        }
    }
}


/**
 * class MyPublisherTest {
 *
 *     private lateinit var kafkaProducer: KafkaProducer<String, SpecificRecordBase>
 *     private lateinit var myPublisher: MyPublisher
 *     private lateinit var dummyValue: SpecificRecordBase
 *
 *     @BeforeEach
 *     fun setUp() {
 *         kafkaProducer = mock()
 *         myPublisher = MyPublisher(kafkaProducer)
 *         dummyValue = mock() // Seu objeto Avro
 *     }
 *
 *     @Test
 *     fun `should call send with correct parameters`() {
 *         val topic = "test-topic"
 *         val key = "key123"
 *
 *         // Chamar publish
 *         myPublisher.publish(topic, key, dummyValue)
 *
 *         // Captura o callback para execução manual
 *         argumentCaptor<BiConsumer<SendResult<String, SpecificRecordBase>, Throwable?>>().apply {
 *             verify(kafkaProducer).send(eq(topic), eq(key), eq(dummyValue), capture())
 *         }
 *     }
 *
 *     @Test
 *     fun `should log success when message is sent successfully`() {
 *         val topic = "topic-success"
 *         val key = "successKey"
 *         val metadata = mock<RecordMetadata> {
 *             on { offset() } doReturn 42L
 *         }
 *         val sendResult = SendResult<String, SpecificRecordBase>(ProducerRecord(topic, key, dummyValue), metadata)
 *
 *         val callbackCaptor = argumentCaptor<BiConsumer<SendResult<String, SpecificRecordBase>, Throwable?>>()
 *
 *         myPublisher.publish(topic, key, dummyValue)
 *
 *         verify(kafkaProducer).send(eq(topic), eq(key), eq(dummyValue), callbackCaptor.capture())
 *
 *         // Simula sucesso na entrega
 *         callbackCaptor.firstValue.accept(sendResult, null)
 *         // Aqui você poderia usar um logger mock para verificar que foi logado corretamente
 *     }
 *
 *     @Test
 *     fun `should log error when message fails to be sent`() {
 *         val topic = "topic-fail"
 *         val key = "failKey"
 *         val exception = RuntimeException("Kafka unavailable")
 *
 *         val callbackCaptor = argumentCaptor<BiConsumer<SendResult<String, SpecificRecordBase>, Throwable?>>()
 *
 *         myPublisher.publish(topic, key, dummyValue)
 *
 *         verify(kafkaProducer).send(eq(topic), eq(key), eq(dummyValue), callbackCaptor.capture())
 *
 *         // Simula erro no envio
 *         callbackCaptor.firstValue.accept(null, exception)
 *         // Aqui você também poderia verificar se a mensagem de erro foi logada
 *     }
 * }?
 */