package io.github.dector.glow.core.components

import arrow.core.Either

interface GlowEngine {

    fun execute(): Either<Throwable, Unit>
}
