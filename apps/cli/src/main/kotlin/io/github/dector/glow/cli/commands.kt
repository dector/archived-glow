package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.github.dector.glow.cli.commands.BuildCommand
import io.github.dector.glow.cli.commands.FishCommand
import io.github.dector.glow.cli.commands.ServeCommand

/**
 * Parse [args] and run specific app.
 */
fun runCli(args: Array<String>) {
    cliCommands().main(args)
}

private fun cliCommands() = run {
    GlowCommand().subcommands(
        BuildCommand(),
        ServeCommand(),
        FishCommand()
    )
}

private class GlowCommand : NoRunCliktCommand(name = "glow")
