plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":glow-common"))
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-ktx"))

    implementation(Deps.javalin)
    implementation(Deps.slf4j_simple)
}
