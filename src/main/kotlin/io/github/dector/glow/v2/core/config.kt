package io.github.dector.glow.v2.core

import io.github.dector.glow.BuildConfig
import io.github.dector.glow.v2.core.Constants.AppVersion


object BuildConfig {

    const val DevMode = false
}

object Constants {

    const val CurrentConfigVersion = "1"

    const val AppVersion = BuildConfig.VERSION
}

val CliHeader = """
    #
    #      _  |  _
    #     (_| | (_) \/\/
    #      _|            v ${AppVersion}
    #
    """.trimMargin("#")