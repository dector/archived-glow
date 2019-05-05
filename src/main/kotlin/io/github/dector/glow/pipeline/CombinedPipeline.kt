package io.github.dector.glow.pipeline


class CombinedPipeline(
        private vararg val components: GlowPipeline) : GlowPipeline {

    override fun execute() {
        components.forEach { it.execute() }
    }
}