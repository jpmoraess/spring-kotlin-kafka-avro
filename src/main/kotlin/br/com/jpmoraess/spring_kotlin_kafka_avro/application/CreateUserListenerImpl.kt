package br.com.jpmoraess.spring_kotlin_kafka_avro.application

import br.com.jpmoraess.spring_kotlin_kafka_avro.application.ports.input.CreateUserListener
import org.springframework.stereotype.Component

@Component
class CreateUserListenerImpl : CreateUserListener {

    override fun userCreated(user: String) {
        TODO("handle user creation here")
    }
}