package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.subcommands
import io.github.dector.glow.cli.commands.BuildCommand
import io.github.dector.glow.cli.commands.FishCommand
import io.github.dector.glow.cli.commands.GlowCommand
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.ui.UiConsole

fun cliCommands() = run {
    val ui = DI.get<UiConsole>()

    GlowCommand().subcommands(
        BuildCommand(ui, glow = DI.get()),
        ServeCommand(),
        FishCommand(ui)
    )
}

fun runCli(args: Array<String>) {
    cliCommands().main(args)
}
