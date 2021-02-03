package com.tcs.integration.common.configuration

import com.tcs.integration.common.configuration.WebMethodsJndiProperties
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import com.tcs.integration.common.messageProvider.kafka.KafkaMessageProvider
import com.tcs.integration.common.messageProvider.um.UMMessageProvider
import com.tcs.integration.common.messageProvider.webm.WebMethodsTopicProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PreDestroy
import kotlin.jvm.Throws

@Configuration
class MessagingConfiguration(private val configProperties: ConfigProperties,
                             private val webMethodsTopicJndiProperties: WebMethodsJndiProperties) {

    @Bean
    @ConditionalOnProperty(name = ["cm.messaging.provider.type"], havingValue = "kafka")
    fun messageProviderKafka(): AbstractMessageProvider {
        println("KafkaMessageProvider")
        return KafkaMessageProvider(configProperties)
    }

    @Bean
    @ConditionalOnProperty(name = ["cm.messaging.provider.type"], havingValue = "um")
    fun messageProviderUM(): AbstractMessageProvider {
        println("UMMessageProvider")
        return UMMessageProvider(configProperties)
    }

    @Bean
    @ConditionalOnProperty(name = ["cm.messaging.provider.type"], havingValue = "webm")
    fun messageProviderWEBMTOPIC(): AbstractMessageProvider {
        println("WebMethodsTopicProvider")
        return WebMethodsTopicProvider(configProperties)
    }

    @PreDestroy
    @Throws(Exception::class)
    fun onDestroy() {
        println("Spring Container is destroyed!")
    }
}
