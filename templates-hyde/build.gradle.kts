plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.kotlinx.html)

    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":glow-common"))

    testImplementation(Deps.kotlin_test)
}
