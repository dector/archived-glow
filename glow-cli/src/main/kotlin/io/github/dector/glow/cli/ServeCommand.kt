package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import io.github.dector.glow.server.Server

class ServeCommand : CliktCommand(name = "serve") {

    override fun run() {
        Server().run()
    }
}
