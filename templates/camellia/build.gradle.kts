plugins {
    kotlin("jvm")
}

version = "0.1-SNAPSHOT"

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.kotlinx_html)

    implementation(project(":core:common"))
    implementation(project(":core:templates"))

    testImplementation(Deps.kotlin_test)
}

