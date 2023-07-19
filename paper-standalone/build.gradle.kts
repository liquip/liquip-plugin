plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    compileOnly(libs.paperApi)
    compileOnly(libs.jacksonDatabind)
    implementation(project(":api"))
    implementation(project(":paper-core"))
    implementation(libs.commandApi)
    implementation(libs.jcougar)
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
