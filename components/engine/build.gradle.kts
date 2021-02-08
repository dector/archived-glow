plugins {
    kotlin("jvm")
}

dependencies {
    implementation(rootProject)

    implementation(project(":component-di"))
    implementation(project(":component-logger"))
    implementation(project(":component-config"))
    implementation(project(":component-ktx"))
    implementation(project(":component-parser-md"))

    implementation(Deps.jtidy)

    testImplementation(Deps.kotest)
}
