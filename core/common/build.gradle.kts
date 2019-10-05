import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs = listOf(
        "-XXLanguage:+InlineClasses"
    )
}
