plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/sqyyy-jar/jcougar-ui")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv(
                "GRADLE_GITHUB_USERNAME"
            )
            password =
                project.findProperty("gpr.key") as String? ?: System.getenv("GRADLE_GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation("dev.jorel:commandapi-shade:8.7.0")
    implementation("com.github.sqyyy:jcougar-ui:0.5.1-alpha")
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "io.github.liquip.paper.standalone.commandapi")
    relocate("com.github.sqyyy.jcougar", "io.github.liquip.paper.standalone.jcougar")
}

tasks.processResources {
    filesMatching("**/paper-plugin.yml") {
        filter {
            return@filter it.replace("\${version}", project.version.toString())
        }
    }
}
