plugins {
    kotlin("jvm")

    id(GradlePlugins.build_config)
    idea
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(project(":component-logger"))
    implementation(project(":component-di"))
    implementation(project(":component-config"))
    implementation(project(":component-engine"))
    implementation(project(":component-ktx"))
    implementation(project(":component-template-common"))
    implementation(project(":component-template-hyde"))

    implementation(Deps.flexmark)

    testImplementation(Deps.kotlin_test)
}
