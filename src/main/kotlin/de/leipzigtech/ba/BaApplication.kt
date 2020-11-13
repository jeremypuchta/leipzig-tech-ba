package de.leipzigtech.ba

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BaApplication

fun main(args: Array<String>) {
	runApplication<BaApplication>(*args)
}
