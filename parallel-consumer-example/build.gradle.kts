plugins {
    id("spring-boot-convention")
    id("io.gatling.gradle") version "3.10.5"
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)

    implementation(project(":gatling-example"))

    implementation("io.confluent.parallelconsumer:parallel-consumer-core:0.5.2.8")
}
