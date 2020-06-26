plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(project(":component-logger"))
    implementation(project(":component-config"))
    implementation(project(":component-ktx"))

    implementation(project(":templates-hyde"))

    implementation(Deps.flexmark)

    implementation(Deps.jtidy)

    testImplementation(Deps.kotlin_test)
}
