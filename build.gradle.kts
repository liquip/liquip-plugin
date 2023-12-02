allprojects {
    group = "io.github.liquip"
    version = "3.1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }
}
