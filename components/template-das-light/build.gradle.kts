plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlinx.html)
    implementation(Deps.pebble)

    implementation(project(":component-di"))
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-template-common"))

    testImplementation(Deps.kotest)
}
