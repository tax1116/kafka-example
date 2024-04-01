package kr.co.taek.dev.parallel.consumer.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParallelConsumerExampleBootstrap

fun main(args: Array<String>) {
    runApplication<ParallelConsumerExampleBootstrap>(*args)
}
