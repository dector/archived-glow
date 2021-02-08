plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":component-config"))
    implementation(project(":component-logger"))
    implementation(project(":component-engine"))
    implementation(project(":component-ktx"))
    implementation(project(":component-parser-md"))

    implementation(project(":component-template-common"))
    //implementation(project(":component-template-hyde"))
    implementation(project(":component-template-das-light"))

    testImplementation(Deps.kotest)
}
