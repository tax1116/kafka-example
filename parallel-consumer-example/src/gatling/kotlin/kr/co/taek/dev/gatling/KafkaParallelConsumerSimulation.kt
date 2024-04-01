package kr.co.taek.dev.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class KafkaParallelConsumerSimulation : Simulation() {
    private val httpProtocol =
        http.baseUrl("http://localhost:8101")
            .contentTypeHeader("application/json")
            .acceptHeader("*/*")

    private val requestBody = StringBody(
        """
            {
                "key": {
                    "type": "STRING",
                    "data": "test"
                },
                "value": {
                    "type": "STRING",
                    "data": "test"
                }
            }
        """.trimIndent()
    )


    private val scenario =
        scenario("KafkaParallelConsumerSimulation")
            .exec(
                http("SimpleTextMessage")
                    .post("/v3/clusters/MkU3OEVBNTcwNTJENDM2Qk/topics/test.parallel.topic/records")
                    .body(requestBody)
                    .check(status().`is`(200))
            )

    init {
        this.setUp(scenario.injectOpen(atOnceUsers(10)))
            .protocols(httpProtocol)
    }
}
