package io.github.dector.glow.build

import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.applyIf
import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.di.configModule
import io.github.dector.glow.di.get
import io.github.dector.glow.div
import io.github.dector.glow.ui.UiConsole
import java.io.File


class BuilderApp private constructor(
    private val ui: UiConsole,
    private val glow: GlowEngine
) {

    fun execute() {
        ui.println(CLI_HEADER)

        glow.execute()
    }

    companion object {
        fun create(
            projectDir: File,
            includeDrafts: Boolean,
            quiet: Boolean
        ): BuilderApp {
            if (!projectFileIn(projectDir).exists())
                TODO("Notify about missing project file nicely")

            val launchConfig = LaunchConfig(
                includeDrafts = includeDrafts
            )
            initApp(projectDir, launchConfig)

            val ui = UiConsole.get
                .applyIf(quiet) { isEnabled = false }

            return BuilderApp(ui, DI.get())
        }
    }
}

private fun initApp(projectDir: File, launchConfig: LaunchConfig) {
    //DI.init()
    DI.resetAction = {
        DI.init()
        DI.modify {
            it.modules(
                configModule(projectDir, launchConfig),
                appModule()
            )
        }
    }
    DI.reset()  // Will call init()
}

private const val ConfigFileName = "website.glow"
private fun projectFileIn(dir: File): File =
    dir / ConfigFileName
