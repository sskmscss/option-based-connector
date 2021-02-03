package com.tcs.service.edt

import com.tcs.service.edt.service.RxBus
import khttp.post
import org.json.JSONObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication(scanBasePackages = ["com.tcs.service.edt", "com.tcs.integration.common"])

class EventDrivenTestApplication

fun main(args: Array<String>) {
	val ctx: ConfigurableApplicationContext = runApplication<EventDrivenTestApplication>(*args)
	// Listen for String events only
	RxBus.listen(JSONObject::class.java).subscribe {
		try {
			when (it.optString("type")) {
				"um" -> {
					println("UM SUBSCRIBED MESSAGE :: " + it.optString("data"))
				}
				"kafka" -> {
					println("KAFKA SUBSCRIBED MESSAGE :: " + it.optString("data"))
				}
				"webm" -> {
					println("WEBM TOPIC SUBSCRIBED MESSAGE :: " + it.optString("data"))
				}
			}
		} catch(e: Exception) {
			println(e)
		}
	}
}
