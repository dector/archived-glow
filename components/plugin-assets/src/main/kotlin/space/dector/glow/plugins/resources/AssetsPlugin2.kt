package space.dector.glow.plugins.resources

import space.dector.glow.config.RuntimeConfig
import space.dector.glow.engine.GlowPipeline
import space.dector.glow.plugins.notes.NotesPluginConfig


class AssetsPlugin2(
    private val config: RuntimeConfig,
    private val runOptions: NotesPluginConfig
) : GlowPipeline {

    override fun onExecute() {
        println("[== Assets ==]")

        if (!runOptions.copyAssets) return

        val src = config.glow.sourceDir.resolve("assets").toFile()
        val dest = config.glow.outputDir.resolve("assets").toFile()

        src.copyRecursively(dest, onError = { file, e ->
            System.err.println("Can't copy asset '${file.absolutePath}' because of ${e.message}")
            OnErrorAction.SKIP
        }, overwrite = config.glow.overrideFiles)
    }
}
