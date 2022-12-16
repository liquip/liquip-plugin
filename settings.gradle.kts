rootProject.name = "liquip"

include("api", "paper-core", "paper-bundled", "paper-standalone", "gui")
project(":gui").name = "liquip-gui"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
