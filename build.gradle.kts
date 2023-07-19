allprojects {
    group = "io.github.liquip"
    version = "3.0.5"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }
}
