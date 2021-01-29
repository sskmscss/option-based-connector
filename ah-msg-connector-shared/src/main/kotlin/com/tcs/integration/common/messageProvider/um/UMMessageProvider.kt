package com.tcs.integration.common.messageProvider.um

import com.pcbsys.nirvana.client.nBaseClientException
import com.pcbsys.nirvana.client.nEventProperties
import com.pcbsys.nirvana.client.nChannel
import com.pcbsys.nirvana.client.nChannelAttributes
import com.pcbsys.nirvana.client.nConsumeEvent
import com.pcbsys.nirvana.client.nEventListener
import com.pcbsys.nirvana.client.nSession
import com.pcbsys.nirvana.client.nSessionAttributes
import com.pcbsys.nirvana.client.nSessionFactory
import com.tcs.integration.common.configuration.ConfigProperties
import com.tcs.integration.common.messageProvider.AbstractMessageProvider
import java.util.concurrent.CopyOnWriteArrayList

class UMMessageProvider (private val configProperties: ConfigProperties): nEventListener, AbstractMessageProvider() {
    private val messages: CopyOnWriteArrayList<String> = CopyOnWriteArrayList<String>()
    var session: nSession?  = null
    var channel: nChannel?  = null
    var sessionSubscribe: nSession? = null

    init {
        if (sessionSubscribe == null) {
            try {
                sessionSubscribe = nSessionFactory.create(nSessionAttributes(arrayOf(configProperties.serverUMUrl)))
                sessionSubscribe!!.init()
                val channelAttribute = nChannelAttributes()
                channelAttribute.name = configProperties.umtopic
                sessionSubscribe!!.findChannel(channelAttribute).addSubscriber(this)
            } catch(e: Exception) { }
        }
    }

    fun getSessionValue(): nSession? {
        if (session == null) {
            val nsa = nSessionAttributes(arrayOf(configProperties.serverUMUrl))
            session = nSessionFactory.create(nsa)
            session?.init()
        }

        return session
    }

    override fun sendMessage(destination: String, payload: Any) {
        if (channel == null)
        {
            val channelAttribute = nChannelAttributes()
            channelAttribute.setName(configProperties.umtopic)
            channel = getSessionValue()!!.findChannel(channelAttribute)

            val props = nEventProperties()
            props.put("data", payload as String)
            channel?.publish(nConsumeEvent("atag", props, "data".toByteArray()))?.use{}
        }
    }

    override fun go(event: nConsumeEvent) {
        try {

            if (event.properties == null) {
                this.messageListener?.receive("um", String(event.eventData))
            } else {
                this.messageListener?.receive("um", event.properties.getString(String(event.eventData)))
            }
            messages.add(String(event.eventData))
            // Not required if topic is created via WM
            // getChannels()?.purgeEvents(event.getEventID(), event.getEventID())
        } catch (e: nBaseClientException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    override fun receive(payload: Any) { }

    override fun subscribeMessage(): String {
        val result = messages.toString()
        messages.clear()

        return result
    }

    private fun Any.use(function: () -> Unit) {
        try {
        } finally {
            session?.close()
            channel = null
            session = null
        }
    }
}
