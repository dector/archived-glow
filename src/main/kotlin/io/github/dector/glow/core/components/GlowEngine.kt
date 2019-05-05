package io.github.dector.glow.core.components

import io.github.dector.glow.core.GlowExecutionResult

interface GlowEngine {

    fun execute(): GlowExecutionResult
}