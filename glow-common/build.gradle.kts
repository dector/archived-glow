plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(project(":logger"))
    implementation(project(":config"))

    implementation(project(":templates-hyde"))

    implementation(Deps.flexmark)

    implementation(Deps.jtidy)

    testImplementation(Deps.kotlin_test)
}
