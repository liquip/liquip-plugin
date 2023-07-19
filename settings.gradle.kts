rootProject.name = "liquip"

include("api", "paper-core", "paper-bundled", "paper-standalone")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("paperApi", "io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
            library("jacksonDatabind", "com.fasterxml.jackson.core:jackson-databind:2.14.2")
            library("commandApi", "dev.jorel:commandapi-bukkit-shade:9.0.3")
            library("jcougar", "com.github.sqyyy:jcougar-ui:0.5.1-alpha")
        }
    }
}
