package io.github.dector.glow.build

import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.core.config.provideProjectConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.buildGlowEngine
import io.github.dector.glow.di.provide
import io.github.dector.glow.engine.GlowEngine
import io.github.dector.glow.ui.UiConsole
import io.github.dector.ktx.applyIf
import io.github.dector.ktx.div
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

            val projectConfig = run {
                val launchConfig = LaunchConfig(
                    includeDrafts = includeDrafts
                )

                provideProjectConfig(projectDir, launchConfig)
            }
            DI.provide(projectConfig)

            val ui = UiConsole.get
                .applyIf(quiet) { isEnabled = false }

            val glowEngine = buildGlowEngine()

            return BuilderApp(ui, glowEngine)
        }
    }
}

private const val ConfigFileName = "website.glow"
private fun projectFileIn(dir: File): File =
    dir / ConfigFileName
