import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application

    kotlin("jvm") version Versions.kotlin

    id(GradlePlugins.build_config) version Versions.build_config
    idea    // Required for build config IDE support

    id(GradlePlugins.shadow) version Versions.shadow
    id(GradlePlugins.versions) version Versions.versions_plugin
}

repositories {
    maven(url = "http://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)

    implementation(project(":logger"))
    implementation(project(":glow-cli"))
}

application {
    @Suppress("UnstableApiUsage")
    mainClassName = "io.github.dector.glow.GlowKt"
}

allprojects {
    group = "io.github.dector.glow"
    version = Config.version

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "1.8"
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

