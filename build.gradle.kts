import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application

    kotlin("jvm") version Versions.kotlin

    id(GradlePlugins.build_config) version Versions.build_config

    id(GradlePlugins.shadow) version Versions.shadow
    id(GradlePlugins.versions) version Versions.versions_plugin

    id(GradlePlugins.detekt) version Versions.detekt
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":component-logger"))

    implementation(project(":app-cli"))
}

application {
    mainClass.set("space.dector.glow.GlowKt")
}

allprojects {
    group = "space.dector.glow"
    version = Config.version

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xinline-classes"
            )
        }
    }

    tasks.withType<Test>().all {
        @Suppress("UnstableApiUsage")
        useJUnitPlatform()
    }
}

detekt {
    config = files("tools/detekt.yml")
    failFast = false
    reports {
        xml.enabled = false
        html.enabled = true
        txt.enabled = false
    }
}

buildConfig {
    buildConfigField("String", "VERSION", "\"${Config.version}\"")
}
