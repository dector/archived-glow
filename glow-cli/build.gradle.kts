plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":app-fish"))
    implementation(project(":app-builder"))
    implementation(project(":app-serve"))

    implementation(project(":config"))
    implementation(project(":glow-common"))
    implementation(project(":glow-server"))

    implementation(Deps.clikt)

    // FIXME remove depdendency on root project
    implementation(rootProject)
}
