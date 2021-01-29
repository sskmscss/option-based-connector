package com.tcs.integration.common.messageProvider.kafka

import com.tcs.integration.common.configuration.ConfigProperties
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.*
import java.io.Closeable
import java.util.concurrent.CopyOnWriteArrayList

@EnableKafka
class KafkaMessageProvider(private val configProperties: ConfigProperties) : AbstractMessageProvider(), Closeable {
    private var producerFactory: DefaultKafkaProducerFactory<String, Any>? = null
    private val messages: CopyOnWriteArrayList<String> = CopyOnWriteArrayList<String>()

    private fun producerFactory(): ProducerFactory<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = configProperties.serverKafkaUrl
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        producerFactory = DefaultKafkaProducerFactory(configProps)
        // producerFactory.isProducerPerThread = true
        return producerFactory as DefaultKafkaProducerFactory<String, Any>
    }

    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    @KafkaListener(topics = ["StoreOrderReference", "com.tcs.service.edt.model.PrepareECMR", "com.tcs.service.model.PostECMR",
        "com.tcs.service.model.ECMRPosted"], groupId = "kafka-subscribe")
    override fun receive(payload: Any) {
        println("KAFKA MESSAGE RECEIVED FROM PROVIDER:: $payload")
        val record: ConsumerRecord<String, Any> = payload as ConsumerRecord<String, Any>
        this.messageListener!!.receive("kafka", record.value())
        messages.add(record.value() as String?)
    }

    override fun subscribeMessage(): String {
        val result = messages.toString()
        messages.clear()
        return result
    }

    override fun sendMessage(destination: String, payload: Any) {
        kafkaTemplate().send(destination, payload).use{}
    }

    override fun close() {
        println("Finalize Close calls here")
    }

    private fun Any.use(function: () -> Unit) {
        try {
        } finally {
            producerFactory?.destroy()
        }
    }
}
