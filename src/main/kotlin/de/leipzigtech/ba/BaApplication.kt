package de.leipzigtech.ba

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@EnableJpaAuditing
class BaApplication

fun main(args: Array<String>) {

	runApplication<BaApplication>(*args)

}
