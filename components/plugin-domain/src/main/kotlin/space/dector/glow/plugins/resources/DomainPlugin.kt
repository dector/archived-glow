package space.dector.glow.plugins.resources

import space.dector.glow.config.RuntimeConfig
import space.dector.glow.engine.GlowPipeline


class DomainPlugin(
    private val config: RuntimeConfig
) : GlowPipeline {

    override fun onExecute() {
        val cname = config.glow.cname ?: return

        config.glow.outputDir
            .resolve("CNAME")
            .toFile()
            .writeText(cname)
    }
}
