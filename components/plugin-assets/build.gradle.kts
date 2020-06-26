plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-plugin-notes"))

    implementation(project(":component-template-common"))
}
