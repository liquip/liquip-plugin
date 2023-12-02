rootProject.name = "liquip"

include("api", "paper-core", "paper-bundled", "paper-standalone")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("paperApi", "io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
            library("jacksonDatabind", "com.fasterxml.jackson.core:jackson-databind:2.14.2")
            library("commandApi", "dev.jorel:commandapi-bukkit-shade:9.2.0")
        }
    }
}

sourceControl {
    gitRepository(uri("https://github.com/sqyyy-jar/jcougar-ui.git")) {
        producesModule("com.github.sqyyy:jcougar-ui")
    }
}
