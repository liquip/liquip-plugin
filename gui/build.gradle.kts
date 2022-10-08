plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.sqyyy"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
