plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.arrow_core_data)

    implementation(project(":glow-common"))
    implementation(project(":glow-server"))

    implementation(Deps.clikt)
}
