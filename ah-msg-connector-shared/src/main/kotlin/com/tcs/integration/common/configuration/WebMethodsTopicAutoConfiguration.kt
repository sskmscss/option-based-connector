package com.tcs.integration.common.configuration

import com.tcs.integration.common.configuration.WebMethodsJndiProperties
import com.pcbsys.nirvana.nSpace.NirvanaContext
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jndi.JndiObjectFactoryBean
import org.springframework.stereotype.Component
import java.util.Properties
import javax.jms.BytesMessage
import javax.jms.ConnectionFactory
import javax.jms.TextMessage
import javax.naming.Context

@EnableJms
@Component
class WebMethodsTopicAutoConfiguration(
        private val webMethodsTopicJndiProperties: WebMethodsJndiProperties
)  {
    @Bean
    @ConditionalOnMissingBean(name = ["topicJmsTemplate"])
    fun topicJmsTemplate() = JmsTemplate().apply {
        isPubSubDomain = true
        connectionFactory = topicConnectionFactory()
    }

    @Bean
    @ConditionalOnMissingBean(name = ["topicJmsListenerContainerFactory"])
    fun topicJmsListenerContainerFactory() = DefaultJmsListenerContainerFactory().apply {
        setPubSubDomain(true)
        setConnectionFactory(topicConnectionFactory())
    }

    fun topicConnectionFactory(): ConnectionFactory {
        val connectionFactory = webMethodsTopicJndiProperties.connectionFactory
        val initialContextFactory = webMethodsTopicJndiProperties.initialContextFactory
        println("Building connection factory {} with initial context factory {}" + connectionFactory + initialContextFactory)

        return with(JndiObjectFactoryBean()) {
            jndiName = connectionFactory
            jndiEnvironment = topicJndiProperties()
            afterPropertiesSet()
            `object` as ConnectionFactory
        }
    }

    fun topicJndiProperties() = Properties().apply {
        put(Context.INITIAL_CONTEXT_FACTORY, webMethodsTopicJndiProperties.initialContextFactory)
        put(Context.PROVIDER_URL, webMethodsTopicJndiProperties.providerUrl)
    }

}
