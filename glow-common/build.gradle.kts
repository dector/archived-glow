plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(Deps.arrow_core_data)

    implementation(Deps.koin)

    implementation(Deps.jtidy)
}

// FIXME Should be enabled only in this project but don't work when doing `glow build`.
/*repositories {
    maven(url = "https://jitpack.io") {
        content { includeGroup("com.github.jtidy") }
    }
}*/
