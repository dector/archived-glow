plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.kotlinx.html)
    implementation(Deps.pebble)

    implementation(project(":component-di"))
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-template-common"))

    testImplementation(Deps.kotest)
}
