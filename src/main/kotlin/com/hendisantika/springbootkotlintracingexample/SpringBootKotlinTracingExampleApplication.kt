package com.hendisantika.springbootkotlintracingexample

import io.micrometer.context.ContextSnapshot
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
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

suspend fun runObserved(name: String, observationRegistry: ObservationRegistry, f: suspend () -> Unit) {
    Mono.deferContextual { contextView ->
        ContextSnapshot.setThreadLocalsFrom(
            contextView,
            ObservationThreadLocalAccessor.KEY
        ).use {
            val observation = Observation.start(name, observationRegistry)
            Mono.just(observation).flatMap {
                mono { f() }
            }.doOnError {
                observation.error(it)
                observation.stop()
            }.doOnSuccess {
                observation.stop()
            }
        }
    }.awaitSingleOrNull()
}

@RestController
class TodoController(
    val observationRegistry: ObservationRegistry,
    val todoRepo: ToDoRepository,
    webClientBuilder: WebClient.Builder
)
