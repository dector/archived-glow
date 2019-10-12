plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.kotlinx_html)

    implementation(project(":glow-common"))
}
