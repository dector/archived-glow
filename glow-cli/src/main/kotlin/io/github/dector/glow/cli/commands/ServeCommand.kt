package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.serve.ServeApp
import io.github.dector.glow.server.Server
import java.io.File

class ServeCommand : CliktCommand(name = "serve") {

    private val project by option("--project")
        .file(exists = true)
        .default(File("."))

    private val includeDrafts by option("--include-drafts")
        .flag()

    override fun run() {
        val config = LaunchConfig(
            includeDrafts = includeDrafts
        )

        initApp(project)

        ServeApp(Server(config)).execute()
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
