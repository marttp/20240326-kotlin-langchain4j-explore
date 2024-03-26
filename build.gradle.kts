plugins {
    kotlin("jvm") version "1.9.23"
}

group = "dev.tpcoder"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.langchain4j:langchain4j:0.28.0")
    implementation("dev.langchain4j:langchain4j-ollama:0.28.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}