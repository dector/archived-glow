object Deps {

    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlin_reflection = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinx_html = "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinx_html}"

    const val slf4j_simple = "org.slf4j:slf4j-simple:${Versions.slf4j_simple}"

    const val arrow_core_data = "io.arrow-kt:arrow-core-data:${Versions.arrow}"
    const val arrow_core_extensions = "io.arrow-kt:arrow-core-extensions:${Versions.arrow}"

    const val koin = "org.koin:koin-core:${Versions.koin}"

    const val clikt = "com.github.ajalt:clikt:${Versions.clikt}"
    const val json = "org.json:json:${Versions.json}"

    const val javalin = "io.javalin:javalin:${Versions.javalin}"

    const val flexmark = "com.vladsch.flexmark:flexmark-all:${Versions.flexmark}"
    const val jmustache = "com.samskivert:jmustache:${Versions.jmustache}"
    const val eo_yaml = "com.amihaiemil.web:eo-yaml:${Versions.eo_yaml}"

    const val kotlin_test = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlin_test}"
}

object GradlePlugins {

    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val build_config = "de.fuerstenau.buildconfig"
    const val shadow = "com.github.johnrengelman.shadow"

    const val versions = "com.github.ben-manes.versions"
}

object Versions {

    const val kotlin = "1.3.50"
    const val kotlinx_html = "0.6.11"

    const val slf4j_simple = "1.7.25"

    const val arrow = "0.9.0"

    const val koin = "2.0.1"

    const val clikt = "1.6.0"
    const val json = "20160810"

    const val javalin = "3.0.0"

    const val flexmark = "0.18.5"
    const val jmustache = "1.13"
    const val eo_yaml = "2.0.1"

    const val kotlin_test = "3.3.2"

    const val shadow = "5.0.0"
    const val versions_plugin = "0.25.0"
    const val build_config = "1.1.8"
}

object Config {

    const val version = "0.2.3-SNAPSHOT"
}
