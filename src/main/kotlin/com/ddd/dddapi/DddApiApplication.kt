package com.ddd.dddapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.ddd.dddapi"])
class DddApiApplication

fun main(args: Array<String>) {
	runApplication<DddApiApplication>(*args)
}
