package space.dector.glow.build

import space.dector.glow.CLI_HEADER
import space.dector.glow.config.LaunchConfig
import space.dector.glow.config.provideProjectConfig
import space.dector.glow.di.DI
import space.dector.glow.di.buildGlowEngine
import space.dector.glow.di.provide
import space.dector.glow.engine.GlowEngine
import space.dector.glow.plugins.notes.NotesPluginConfig
import space.dector.glow.ui.UiConsole
import space.dector.ktx.applyIf
import space.dector.ktx.div
import java.io.File


class BuilderApp internal constructor(
    private val ui: UiConsole,
    private val glow: GlowEngine
) {

    fun execute() {
        ui.println(CLI_HEADER)

        glow.execute()
    }

    companion object
}

/**
 * Construct app instance with requested configuration.
 *
 * @param projectDir path to website directory (should contain website configuration file)
 * @param includeDrafts set to `true` to render drafts
 * @param quiet set to `true` to disable any output
 */
fun BuilderApp.Companion.create(
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
    DI.provide(NotesPluginConfig())

    val ui = UiConsole.get
        .applyIf(quiet) { isEnabled = false }

    val glowEngine = buildGlowEngine()

    return BuilderApp(ui, glowEngine)
}

private const val ConfigFileName = "website.glow"
private fun projectFileIn(dir: File): File =
    dir / ConfigFileName
