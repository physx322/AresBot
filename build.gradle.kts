plugins {
    id("java")
}

group = "org.clicdroit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.2")
    implementation("org.apache.logging.log4j:log4j:2.17.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
}

tasks.test {
    useJUnitPlatform()
}