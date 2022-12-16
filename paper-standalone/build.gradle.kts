plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    implementation(project(mapOf("path" to ":api")))
    implementation(project(mapOf("path" to ":paper-core")))
    implementation(project(mapOf("path" to ":liquip-gui")))
    implementation("dev.jorel:commandapi-shade:8.5.1")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "io.github.liquip.commandapi")
}
