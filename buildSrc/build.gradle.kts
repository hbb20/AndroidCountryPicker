plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.apache.commons:commons-csv:1.7")
}
repositories {
    maven(url = "https://maven.google.com")
    mavenCentral()
}
