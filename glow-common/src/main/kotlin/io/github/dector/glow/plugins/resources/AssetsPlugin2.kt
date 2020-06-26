package io.github.dector.glow.plugins.resources

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.engine.GlowPipeline


class AssetsPlugin2(
    private val config: RuntimeConfig,
    private val runOptions: NotesPluginConfig
) : GlowPipeline {

    override fun execute() {
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
