plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.paperApi)
    compileOnly(libs.jacksonDatabind)
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation(libs.commandApi)
    implementation("com.github.sqyyy:jcougar-ui:0.5.1-alpha") {
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
