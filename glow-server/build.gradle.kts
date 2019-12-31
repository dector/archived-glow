plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":glow-common"))

    implementation(Deps.javalin)
    implementation(Deps.slf4j_simple)

    implementation(Deps.koin)
}
