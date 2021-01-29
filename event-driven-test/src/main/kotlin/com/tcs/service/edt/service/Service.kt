package com.tcs.service.edt.service

import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class Service {

    fun publishMessage(serviceCall: AbstractMessageProvider, topic: String, msg: Any) {
        println("TOPIC $topic")
        serviceCall.sendMessage(topic, msg)
    }

    fun subscribeMessage(serviceCall: AbstractMessageProvider, type: String): String? {
        return serviceCall.subscribeMessage()
    }
}
