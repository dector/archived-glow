plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":component-di"))
    implementation(project(":component-logger"))
    implementation(project(":component-config"))
    implementation(project(":component-ktx"))
    implementation(project(":component-engine"))
    implementation(project(":component-parser-md"))
    implementation(project(":component-plugin-notes"))
    implementation(project(":component-plugin-assets"))
    implementation(project(":component-plugin-theme-assets"))
    implementation(project(":component-plugin-domain"))
    implementation(project(":component-template-common"))

    implementation(Deps.jtidy)
}
