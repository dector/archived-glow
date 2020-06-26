package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.build.BuilderApp
import java.io.File

class BuildCommand : CliktCommand(name = "build") {

    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    private val quiet by option("-q", "--quiet",
        help = "Don't print anything").flag()

    override fun run() {
        BuilderApp.create(
            projectDir = project,
            quiet = quiet
        ).execute()
    }
}
