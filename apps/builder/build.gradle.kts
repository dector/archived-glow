plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":config"))
    implementation(project(":glow-common"))

    implementation(Deps.flexmark)

    // FIXME remove this dependcy for di
    implementation(rootProject)
}
