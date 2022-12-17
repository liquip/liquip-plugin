plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/liquip/liquip-plugin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation(project(":liquip-gui"))
    implementation("dev.jorel:commandapi-shade:8.7.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.0")
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "io.github.liquip.commandapi")
}
