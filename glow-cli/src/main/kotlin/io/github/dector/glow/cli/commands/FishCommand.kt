package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.fish.FishApp

class FishCommand : CliktCommand(name = "fish") {

    override fun run() {
        FishApp(DI.get()).execute()
    }
}
