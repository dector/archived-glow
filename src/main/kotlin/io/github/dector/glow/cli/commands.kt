package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.github.dector.glow.cli.commands.BuildCommand
import io.github.dector.glow.cli.commands.FishCommand
import io.github.dector.glow.cli.commands.GlowCommand
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.server.Server
import io.github.dector.glow.ui.UiConsole

class ServeCommand : CliktCommand(name = "serve") {

    override fun run() {
        Server().run()
    }
}

fun cliCommands() = run {
    val ui = DI.get<UiConsole>()

    GlowCommand().subcommands(
        BuildCommand(ui, glow = DI.get()),
        ServeCommand(),
        FishCommand(ui)
    )
}
