package com.tcs.service.edt.integration

import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import com.tcs.service.edt.message.Reactor
import com.tcs.service.edt.service.Service
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
class WebmTopic(private val webmTopic: AbstractMessageProvider, private val service: Service, private val reactor: Reactor) {
    @Value("\${cm.messaging.umtopic}")
    lateinit var topic: String

    init {
        webmTopic.messageListener = this.reactor
    }

    fun publishMessage(payload: Any) {
//        service.publishMessage(um, topic, payload)
    }

    fun subscribeMessage(type: String): String? {
        return service.subscribeMessage(webmTopic, type)
    }
}