plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":config"))
    implementation(project(":glow-common"))

    // FIXME remove this dependcy for di
    implementation(rootProject)
}
