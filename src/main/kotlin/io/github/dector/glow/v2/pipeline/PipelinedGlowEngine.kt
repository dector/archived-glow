package io.github.dector.glow.v2.pipeline

import io.github.dector.glow.v2.core.GlowExecutionResult
import io.github.dector.glow.v2.core.components.GlowEngine


class PipelinedGlowEngine : GlowEngine {

    private val pipeline = GlowPipeline()

    override fun execute(): GlowExecutionResult {

        // Do nothing

        return GlowExecutionResult.Success
    }
}

class GlowPipeline {

    private val dataProviders = mutableListOf<DataProvider3>()
    private val dataRenderers = mutableListOf<DataRenderer3>()
    private val dataPublishers = mutableListOf<DataPublisher3>()
}

interface DataProvider3 {}
interface DataRenderer3 {}
interface DataPublisher3 {}