package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.ui.UiConsole

class BuildCommand(
    private val console: UiConsole,
    private val glow: GlowEngine
) : CliktCommand(name = "build") {

    private val quiet by option("-q", "--quiet",
        help = "Don't print anything").flag()

    override fun run() {
        if (quiet) console.isEnabled = false

        console.println(CLI_HEADER)

        glow.execute()
    }
}
