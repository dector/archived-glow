plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.hjson)

    // FIXME temporary workaround to use `LegacyProjectConfig`
    implementation(project(":glow-common"))
}
