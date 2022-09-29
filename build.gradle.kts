plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.3.8"
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
    compileOnly("dev.triumphteam:triumph-gui:3.1.3")
    compileOnly("com.typesafe:config:1.4.2")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
