plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.hjson)

    implementation(project(":component-ktx"))
}
