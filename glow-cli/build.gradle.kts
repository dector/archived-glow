plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":app-fish"))

    implementation(project(":config"))
    implementation(project(":glow-common"))
    implementation(project(":glow-server"))

    implementation(Deps.clikt)
}
