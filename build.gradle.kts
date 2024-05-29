allprojects {
    group = "io.github.liquip"
    version = "4.0.0-beta.1"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }
}
