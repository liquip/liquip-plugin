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
    compileOnly("com.typesafe:config:1.4.2")
    compileOnly("dev.jorel:commandapi-annotations:8.5.1")
    annotationProcessor("dev.jorel:commandapi-annotations:8.5.1")
    implementation("dev.jorel:commandapi-shade:8.5.1")
    implementation(project(mapOf("path" to ":liquip-gui")))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    shadowJar {
        relocate("dev.jorel.commandapi", "com.github.sqyyy.liquip.commandapi")
    }
}
