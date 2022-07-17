plugins {
    id("java")
}

group = "com.imaginarycity"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Discord Library
    implementation("net.dv8tion", "JDA", "5.0.0-alpha.13") {
        exclude(module = "opus-java")
    }

    // JSON
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.13.2")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha16")

    // Testing
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}