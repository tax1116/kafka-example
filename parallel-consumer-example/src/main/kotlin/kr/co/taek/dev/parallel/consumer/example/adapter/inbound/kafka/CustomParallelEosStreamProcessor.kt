package kr.co.taek.dev.parallel.consumer.example.adapter.inbound.kafka

import io.confluent.parallelconsumer.ParallelConsumerOptions
import io.confluent.parallelconsumer.ParallelEoSStreamProcessor
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger { }

class CustomParallelEosStreamProcessor<K, V>(
    newOptions: ParallelConsumerOptions<K, V>,
) : ParallelEoSStreamProcessor<K, V>(newOptions) {
    @Synchronized
    fun replaceWorkerPoolSize(poolSize: Int) {
        if (options.maxConcurrency < poolSize) {
            throw IllegalArgumentException("Pool size must be less than or equal to max concurrency")
        }

        log.info { "Replace poolSize to $poolSize" }
        val workerThreadPool = this.workerThreadPool.get()

        if (workerThreadPool.corePoolSize < poolSize) {
            workerThreadPool.maximumPoolSize = poolSize
            workerThreadPool.corePoolSize = poolSize
        } else {
            workerThreadPool.corePoolSize = poolSize
            workerThreadPool.maximumPoolSize = poolSize
        }
    }
}
