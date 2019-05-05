package io.github.dector.glow.v2.pipeline


class CombinedPipeline(
        private vararg val components: GlowPipeline) : GlowPipeline {

    override fun execute() {
        components.forEach { it.execute() }
    }
}