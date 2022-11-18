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
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
