object Deps {

    val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val kotlin_reflection = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val kotlinx_html = "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinx_html}"

    val slf4j_simple = "org.slf4j:slf4j-simple:${Versions.slf4j_simple}"

    val arrow_core_data = "io.arrow-kt:arrow-core-data:${Versions.arrow}"
    val arrow_core_extensions = "io.arrow-kt:arrow-core-extensions:${Versions.arrow}"

    val koin = "org.koin:koin-core:${Versions.koin}"

    val clikt = "com.github.ajalt:clikt:${Versions.clikt}"
    val jcommander = "com.beust:jcommander:${Versions.jcommander}"
    val json = "org.json:json:${Versions.json}"

    val javalin = "io.javalin:javalin:${Versions.javalin}"

    val flexmark = "com.vladsch.flexmark:flexmark-all:${Versions.flexmark}"
    val jmustache = "com.samskivert:jmustache:${Versions.jmustache}"
    val eo_yaml = "com.amihaiemil.web:eo-yaml:${Versions.eo_yaml}"

    val kotlin_test = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlin_test}"
}

object GradlePlugins {

    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val build_config = "gradle.plugin.de.fuerstenau:BuildConfigPlugin:${Versions.build_config}"
    val shadow_jar = "com.github.jengelman.gradle.plugins:shadow:${Versions.shadow_jar}"
    val versions_plugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.versions_plugin}"
}

object Versions {

    val shadow_jar = "5.0.0"
    val versions_plugin = "0.21.0"
    val build_config = "1.1.8"

    val kotlin = "1.3.50"
    val kotlinx_html = "0.6.11"

    val slf4j_simple = "1.7.25"

    val arrow = "0.9.0"

    val koin = "2.0.1"

    val clikt = "1.6.0"
    val jcommander = "1.48"
    val json = "20160810"

    val javalin = "3.0.0"

    val flexmark = "0.18.5"
    val jmustache = "1.13"
    val eo_yaml = "2.0.1"

    val kotlin_test = "3.3.2"
}
