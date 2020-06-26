package io.github.dector.glow.pipeline

import io.github.dector.glow.logger.logger


class PluggablePipeline(
    private vararg val plugins: GlowPipeline
) : GlowPipeline {

    override fun execute() {
        plugins.forEach {
            logger().info("Executing plugin: ${it::class.simpleName}")

            try {
                it.execute()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
