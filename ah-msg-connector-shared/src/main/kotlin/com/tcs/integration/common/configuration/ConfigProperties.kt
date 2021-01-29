package com.tcs.integration.common.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ConfigProperties() {

    @Value("\${cm.int.kafka.server-url}")
    lateinit var serverKafkaUrl: String

    @Value("\${cm.messaging.umtopic}")
    lateinit var umtopic: String

    @Value("\${cm.int.um.server-url}")
    lateinit var serverUMUrl: String
}