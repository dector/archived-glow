plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(Deps.arrow_core_data)

    implementation(Deps.koin)

    // Use dependency from maven repository when jtidy will be published
    //implementation(Deps.jtidy)
    implementation(files("libs/jtidy-1.0.2-SNAPSHOT.jar"))
}
