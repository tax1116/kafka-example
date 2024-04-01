plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "kafka-example"

include(
    "parallel-consumer-example",
    "gatling-example"
)
