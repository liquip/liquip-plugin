rootProject.name = "liquip"

include("api", "paper-core", "paper-bundled", "paper-standalone")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
