plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-ktx"))
    implementation(project(":component-parser-md"))

    implementation(project(":component-template-common"))
    implementation(project(":component-template-hyde"))

    testImplementation(Deps.kotest)
}
