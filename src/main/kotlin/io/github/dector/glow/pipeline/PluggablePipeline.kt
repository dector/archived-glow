package io.github.dector.glow.pipeline


class PluggablePipeline(
        private vararg val plugins: GlowPipeline
) : GlowPipeline {

    override fun execute() {
        plugins.forEach { it.execute() }
    }
}