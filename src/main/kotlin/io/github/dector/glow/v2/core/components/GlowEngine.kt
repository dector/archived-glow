package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.GlowExecutionResult

interface GlowEngine {

    fun execute(): GlowExecutionResult
}