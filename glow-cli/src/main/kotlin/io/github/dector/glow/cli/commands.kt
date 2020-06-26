package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.subcommands
import io.github.dector.glow.cli.commands.BuildCommand
import io.github.dector.glow.cli.commands.FishCommand
import io.github.dector.glow.cli.commands.GlowCommand
import io.github.dector.glow.cli.commands.ServeCommand


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
