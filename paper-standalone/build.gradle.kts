plugins {
    java
    id("io.github.goooler.shadow") version "8.1.7"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.jacksonDatabind)
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation(libs.cloud)
    implementation(libs.jcougarUi) {
        version { branch = "main" }
    }
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

tasks.runServer {
    minecraftVersion("1.20.6")
}