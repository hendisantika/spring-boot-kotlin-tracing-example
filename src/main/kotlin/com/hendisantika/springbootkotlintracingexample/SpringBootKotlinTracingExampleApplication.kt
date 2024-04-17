package com.hendisantika.springbootkotlintracingexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@SpringBootApplication
class SpringBootKotlinTracingExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinTracingExampleApplication>(*args)
}

@Table("todo")
data class ToDo(
    @Id
    val id: Long = 0,
    val title: String,
)
