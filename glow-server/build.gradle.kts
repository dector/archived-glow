plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":component-config"))
    implementation(project(":component-ktx"))
    implementation(project(":glow-common"))

    implementation(Deps.javalin)
    implementation(Deps.slf4j_simple)
}
