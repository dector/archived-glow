package io.github.dector.glow.core.components

import arrow.core.Either

interface GlowEngine {

    fun execute(): Result<Unit>

    fun executeNew(): Either<Throwable, Unit> = run {
        val result: Result<Unit> = execute()

        if (result.isSuccess) Either.right(result.getOrThrow())
        else Either.Left(result.exceptionOrNull()!!)
    }
}
