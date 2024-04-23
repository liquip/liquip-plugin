rootProject.name = "liquip"

include("api", "paper-core", "paper-bundled", "paper-standalone")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

sourceControl {
    gitRepository(uri("https://github.com/sqyyy-jar/jcougar-ui.git")) {
        producesModule("com.github.sqyyy:jcougar-ui")
    }
}
