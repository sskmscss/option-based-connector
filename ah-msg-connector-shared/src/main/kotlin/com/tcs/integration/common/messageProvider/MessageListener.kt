package com.tcs.integration.common.messageProvider

interface MessageListener {
    fun receive(type: String, payload: Any)
}
