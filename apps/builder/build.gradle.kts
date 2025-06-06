plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":component-di"))
    implementation(project(":component-logger"))
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-engine-builder"))
    implementation(project(":component-ktx"))
    implementation(project(":component-plugin-notes"))

    implementation(Deps.flexmark)

    implementation(rootProject)
}
