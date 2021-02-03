package com.tcs.integration.common.messageProvider.webm

import com.tcs.integration.common.configuration.ConfigProperties
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import javax.jms.BytesMessage
import javax.jms.TextMessage


@EnableJms
class WebMethodsTopicProvider(
        private val configProperties: ConfigProperties
) : AbstractMessageProvider()  {

    @JmsListener(containerFactory = "topicJmsListenerContainerFactory", destination = "\${webmethods.topic.name}")
    override fun receive(payload: Any) {
        if (payload is TextMessage) {
            val messageText = payload.text
            println("A new slug has arrived: $messageText")

        } else if (payload is BytesMessage) {
            var message = payload
            val charset = Charsets.UTF_8
            println(message.bodyLength.toInt())
            val data = ByteArray(message.bodyLength.toInt())
            message.readBytes(data)
            println(data.toString(charset))
            var messageText = String(data)
            println(messageText)
        }
        println("LISTENER CALLED")

    }

    override fun sendMessage(destination: String, payload: Any) {
    }

    override fun subscribeMessage(): String {
        return ""
    }

}
