package com.tcs.service.edt.integration

import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import com.tcs.service.edt.message.Reactor
import com.tcs.service.edt.service.Service
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
class Kafka(private val kafka: AbstractMessageProvider, private val service: Service, private val reactor: Reactor) {
    @Value("\${cm.messaging.kafkatopic}")
    lateinit var topic: String

    init {
        kafka.messageListener = this.reactor
    }

    fun publishMessage(payload: Any) {
        service.publishMessage(kafka, topic, payload)
    }

    fun subscribeMessage(type: String): String? {
        return service.subscribeMessage(kafka, type)
    }
}
