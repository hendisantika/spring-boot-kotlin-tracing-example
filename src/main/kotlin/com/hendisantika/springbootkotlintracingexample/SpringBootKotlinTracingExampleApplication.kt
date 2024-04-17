package com.hendisantika.springbootkotlintracingexample

import io.micrometer.context.ContextSnapshot
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import reactor.core.publisher.Mono

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

interface ToDoRepository : CoroutineCrudRepository<ToDo, Long>

suspend inline fun observeCtx(crossinline f: () -> Unit) {
    Mono.deferContextual { contextView ->
        ContextSnapshot.setThreadLocalsFrom(
            contextView,
            ObservationThreadLocalAccessor.KEY
        ).use {
            f()
            Mono.empty<Unit>()
        }
    }.awaitSingleOrNull()
}
