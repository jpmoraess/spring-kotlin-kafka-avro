package br.com.jpmoraess.spring_kotlin_kafka_avro.common.kafka.producer.exception

class KafkaProducerException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}