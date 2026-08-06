plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.ares.Main"  // remplace par ton vrai chemin de classe
        }
    }
}

group = "org.ares"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.2")
    implementation("org.apache.logging.log4j:log4j:2.17.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

tasks.test {
    useJUnitPlatform()
}