package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import io.github.dector.glow.fish.FishApp

internal class FishCommand : CliktCommand(name = "fish") {

    override fun run() {
        FishApp.create().execute()
    }
}
