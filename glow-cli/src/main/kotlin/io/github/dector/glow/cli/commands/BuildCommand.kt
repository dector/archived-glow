package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.build.BuilderApp
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.di.get
import io.github.dector.glow.ui.UiConsole
import java.io.File

class BuildCommand : CliktCommand(name = "build") {

    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    private val quiet by option("-q", "--quiet",
        help = "Don't print anything").flag()

    override fun run() {
        initApp(project)

        val ui = DI.get<UiConsole>()
        if (quiet) ui.isEnabled = false

        BuilderApp(ui, DI.get()).execute()
    }

    private fun initApp(projectDir: File) {
        //DI.init()
        DI.resetAction = {
            DI.init()
            DI.modify {
                it.modules(appModule(projectDir))
            }
        }
        DI.reset()  // Will call init()
    }
}
