plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":app-fish"))
    implementation(project(":app-builder"))
    implementation(project(":app-serve"))

    implementation(Deps.clikt)
}
