@file:Suppress("MayBeConstant")

object Deps {

    val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    object kotlinx {
        val html = "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinx.html}"
    }

    val slf4j_simple = "org.slf4j:slf4j-simple:${Versions.slf4j_simple}"

    val clikt = "com.github.ajalt:clikt:${Versions.clikt}"
    val hjson = "org.hjson:hjson:${Versions.hjson}"

    val javalin = "io.javalin:javalin:${Versions.javalin}"

    val jtidy = "com.github.jtidy:jtidy:${Versions.jtidy}"
    val flexmark = "com.vladsch.flexmark:flexmark-all:${Versions.flexmark}"

    val kotest = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotest}"
}

object GradlePlugins {

    val build_config = "de.fuerstenau.buildconfig"
    val shadow = "com.github.johnrengelman.shadow"

    val versions = "com.github.ben-manes.versions"
    val detekt = "io.gitlab.arturbosch.detekt"
}

object Versions {

    val kotlin = "1.3.72"

    object kotlinx {
        val html = "0.7.1"
    }

    val slf4j_simple = "1.7.26"

    val clikt = "2.4.0"
    val hjson = "3.0.0"

    val javalin = "3.9.1"

    val jtidy = "1.0.2"
    val flexmark = "0.62.2"

    val kotest = "3.4.2"

    val shadow = "5.1.0"
    val versions_plugin = "0.28.0"
    val build_config = "1.1.8"
    val detekt = "1.9.0"
}

object Config {

    val version = "0.3-SNAPSHOT"
}
