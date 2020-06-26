package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.serve.ServeApp
import java.io.File

class ServeCommand : CliktCommand(name = "serve") {

    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    private val includeDrafts by option("--include-drafts")
        .flag()

    override fun run() {
        ServeApp.create(
            projectDir = project,
            includeDrafts = includeDrafts
        ).execute()
    }
}
