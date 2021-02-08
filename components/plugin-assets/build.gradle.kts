plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-plugin-notes"))

    implementation(project(":component-template-common"))
}
