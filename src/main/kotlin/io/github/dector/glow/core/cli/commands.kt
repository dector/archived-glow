package io.github.dector.glow.core.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.dector.glow.core.di.DI
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.logger.disableUiLogger
import io.github.dector.glow.v2.core.CliHeader
import io.github.dector.glow.v2.core.components.GlowEngine

class GlowCommand : CliktCommand(name = "glow") {
    override fun run() {}
}

class BuildCommand : CliktCommand(name = "build") {
    val quiet by option("-q", "--quiet",
            help = "Don't print anything").flag()

    override fun run() {
        if (quiet) disableUiLogger()

        UILogger.info(CliHeader)

        DI.get<GlowEngine>().execute()
    }
}

class FishCommand : CliktCommand(name = "fish") {

    private val fish = """
          /`·.¸
         /¸...¸`:·
     ¸.·´  ¸   `·.¸.·´)
    : © ):´;      ¸  {
     `·.¸ `·  ¸.·´\`·¸)
         `\\´´\¸.·´     Hi, there!"""

    override fun run() {
        UILogger.info(fish)
    }
}

fun cliCommands() = GlowCommand()
        .subcommands(BuildCommand(), FishCommand())