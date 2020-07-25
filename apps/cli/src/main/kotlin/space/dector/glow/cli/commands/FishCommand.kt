package space.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import space.dector.glow.fish.FishApp

internal class FishCommand : CliktCommand(name = "fish") {

    override fun run() {
        FishApp.create().execute()
    }
}
