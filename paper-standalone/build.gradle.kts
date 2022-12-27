plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation(project(":liquip-gui"))
    implementation("dev.jorel:commandapi-shade:8.7.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.0")
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "io.github.liquip.commandapi")
}
