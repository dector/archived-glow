object Deps {

    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlin_reflection = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinx_html = "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinx_html}"

    const val slf4j_simple = "org.slf4j:slf4j-simple:${Versions.slf4j_simple}"

    const val arrow_core_data = "io.arrow-kt:arrow-core-data:${Versions.arrow}"

    const val koin = "org.koin:koin-core:${Versions.koin}"

    const val clikt = "com.github.ajalt:clikt:${Versions.clikt}"
    const val json = "org.json:json:${Versions.json}"
    const val hjson = "org.hjson:hjson:${Versions.hjson}"

    const val javalin = "io.javalin:javalin:${Versions.javalin}"

    const val rome = "com.rometools:rome:${Versions.rome}"

    const val jtidy = "com.github.jtidy:jtidy:${Versions.jtidy}"
    const val flexmark = "com.vladsch.flexmark:flexmark-all:${Versions.flexmark}"
    const val eo_yaml = "com.amihaiemil.web:eo-yaml:${Versions.eo_yaml}"

    const val kotlin_test = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlin_test}"
}

object GradlePlugins {

    const val build_config = "de.fuerstenau.buildconfig"
    const val shadow = "com.github.johnrengelman.shadow"

    const val versions = "com.github.ben-manes.versions"
}

object Versions {

    const val kotlin = "1.3.50"
    const val kotlinx_html = "0.6.12"

    const val slf4j_simple = "1.7.26"

    const val arrow = "0.10.0"

    const val koin = "2.1.0-alpha-1"

    const val clikt = "2.2.0"
    const val json = "20190722"
    const val hjson = "3.0.0"

    const val javalin = "3.5.0"

    const val rome = "1.12.2"
    const val jtidy = "1.0.1"
    const val flexmark = "0.50.40"
    const val eo_yaml = "2.0.1"

    const val kotlin_test = "3.4.2"

    const val shadow = "5.1.0"
    const val versions_plugin = "0.25.0"
    const val build_config = "1.1.8"
}

object Config {

    const val version = "0.2.3-SNAPSHOT"
}
