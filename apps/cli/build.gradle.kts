plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app-fish"))
    implementation(project(":app-builder"))
    implementation(project(":app-serve"))

    implementation(Deps.clikt)
}
