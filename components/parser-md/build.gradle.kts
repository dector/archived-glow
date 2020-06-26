plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    api(Deps.flexmark)

    testImplementation(Deps.kotest)
}
