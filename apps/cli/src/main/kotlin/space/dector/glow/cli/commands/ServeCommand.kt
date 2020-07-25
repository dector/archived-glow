package space.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import space.dector.glow.serve.ServeApp
import space.dector.glow.serve.create
import java.io.File

internal class ServeCommand : CliktCommand(name = "serve") {

    /** Path to project dir */
    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    /** Option to override default drafts excluding behaviour */
    private val includeDrafts by option("--include-drafts")
        .flag()

    override fun run() {
        ServeApp.create(
            projectDir = project,
            includeDrafts = includeDrafts
        ).execute()
    }
}
