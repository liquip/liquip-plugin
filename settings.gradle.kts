rootProject.name = "liquip"

include("core", "example-plugin", "gui")

project(":core").name = "liquip-core"
project(":gui").name = "liquip-gui"
project(":example-plugin").name = "liquip-example-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
