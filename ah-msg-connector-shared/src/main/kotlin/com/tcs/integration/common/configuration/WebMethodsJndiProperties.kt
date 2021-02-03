package com.tcs.integration.common.configuration

import org.springframework.stereotype.Component

//@Component
class WebMethodsJndiProperties {
    lateinit var initialContextFactory: String
    lateinit var connectionFactory: String
    lateinit var providerUrl: String
}
