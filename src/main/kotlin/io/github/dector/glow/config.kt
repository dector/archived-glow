package io.github.dector.glow

import io.github.dector.glow.Constants.AppVersion


// TODO move it to BuildConfig
object BuildSetup {

    const val DevMode = false
}

object Constants {

    const val CurrentConfigVersion = "1"

    const val AppVersion = BuildConfig.VERSION
}

val CLI_HEADER = """
    #
    #      _  |  _
    #     (_| | (_) \/\/
    #      _|            v ${AppVersion}
    #
    """.trimMargin("#")