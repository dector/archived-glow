plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(project(":logger"))
    implementation(project(":config"))

    implementation(Deps.koin)

    implementation(Deps.jtidy)
}
