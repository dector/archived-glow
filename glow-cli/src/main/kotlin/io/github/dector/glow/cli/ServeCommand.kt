package io.github.dector.glow.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.server.Server

class ServeCommand : CliktCommand(name = "serve") {

    private val project by option("--project")
        .file(exists = true)

    private val includeDrafts by option("--include-drafts")
        .flag()

    override fun run() {
        val config = LaunchConfig(
            includeDrafts = includeDrafts
        )

        Server(config).run()
    }
}
