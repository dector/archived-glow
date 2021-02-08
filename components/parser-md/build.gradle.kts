plugins {
    kotlin("jvm")
}

dependencies {
    api(Deps.flexmark)

    testImplementation(Deps.kotest)
}
