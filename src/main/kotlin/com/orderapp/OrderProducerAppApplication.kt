package com.orderapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderProducerAppApplication

fun main(args: Array<String>) {
    runApplication<OrderProducerAppApplication>(*args)
}
