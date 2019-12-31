plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(Deps.koin)

    implementation(Deps.jtidy)
}
