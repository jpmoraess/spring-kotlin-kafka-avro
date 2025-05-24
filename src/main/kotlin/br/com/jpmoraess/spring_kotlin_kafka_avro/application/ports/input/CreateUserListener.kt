package br.com.jpmoraess.spring_kotlin_kafka_avro.application.ports.input

interface CreateUserListener {

    fun userCreated(user: String)
}