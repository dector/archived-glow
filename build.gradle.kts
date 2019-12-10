import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    repositories {
        jcenter()
    }

    dependencies {
    }
}

plugins {
    application

    kotlin("jvm") version Versions.kotlin

    id(GradlePlugins.build_config) version Versions.build_config
    idea    // Required for build config IDE support

    id(GradlePlugins.shadow) version Versions.shadow
    id(GradlePlugins.versions) version Versions.versions_plugin
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
    maven(url = "http://oss.sonatype.org/content/groups/public/")

    // FIXME shouldn't be duplicated here but `./gradlew run` in failing.
    maven(url = "https://jitpack.io") {
        content { includeGroup("com.github.jtidy") }
    }
}

dependencies {
    implementation(Deps.kotlin_stdlib_jdk8)
    implementation(Deps.kotlin_reflection)  // can be removed later

    implementation(project(":glow-common"))
    implementation(project(":glow-cli"))

    implementation(project(":templates-hyde"))
    implementation(Deps.kotlinx_html)

    implementation(Deps.slf4j_simple)

    implementation(Deps.arrow_core_data)

    implementation(Deps.koin)

    implementation(Deps.json)
    implementation(Deps.hjson)

    implementation(Deps.javalin)

    implementation(Deps.rome)

    implementation(Deps.flexmark)
    implementation(Deps.eo_yaml)

    testImplementation(Deps.kotlin_test)
}


application {
    mainClassName = "io.github.dector.glow.GlowKt"
}

//shadowJar {
//    classifier = null
//}

//task distrib(dependsOn: "shadowJar") {
//    doLast {
//        def distribFile = file("./distrib/${jar.archiveName}")
//
//        copy {
//            from jar.archivePath
//            into distribFile.parentFile
//        }
//
//        def relativePath = file(".").toURI().relativize(distribFile.toURI())
//        println "Distribution saved to ./$relativePath"
//    }
//}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-XXLanguage:+InlineClasses",
            "-XXLanguage:+NewInference",
            "-Xallow-result-return-type"
        )
    }
}

allprojects {
    group = "io.github.dector.glow"
    version = Config.version

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test>().all {
        useJUnitPlatform()
    }
}

