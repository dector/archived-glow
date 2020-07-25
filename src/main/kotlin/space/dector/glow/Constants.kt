package space.dector.glow

import space.dector.glow.BuildConfig
import space.dector.glow.Constants.AppVersion

object Constants {

    const val CurrentConfigVersion = "1"

    const val AppVersion = BuildConfig.VERSION
}

val CLI_HEADER = """
    #
    #      _  |  _
    #     (_| | (_) \/\/
    #      _|            v $AppVersion
    #
    """.trimMargin("#")
