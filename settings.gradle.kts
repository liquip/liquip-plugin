include("core", "example-plugin")

rootProject.name = "liquip"

project(":core").name = "liquip-core"
project(":example-plugin").name = "liquip-example-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
