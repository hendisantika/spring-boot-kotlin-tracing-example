package com.hendisantika.springbootkotlintracingexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootKotlinTracingExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinTracingExampleApplication>(*args)
}
