package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.build.BuilderApp
import io.github.dector.glow.build.create
import java.io.File

internal class BuildCommand : CliktCommand(name = "build") {

    /** Path to project dir */
    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    /** Option to override default drafts excluding behaviour */
    private val includeDrafts by option("--include-drafts")
        .flag()

    /** Produce no output */
    private val quiet by option("-q", "--quiet",
        help = "Don't print anything").flag()

    override fun run() {
        BuilderApp.create(
            projectDir = project,
            includeDrafts = includeDrafts,
            quiet = quiet
        ).execute()
    }
}
