plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":component-config"))
    implementation(project(":component-logger"))
    implementation(project(":glow-common"))

    implementation(Deps.flexmark)
}
