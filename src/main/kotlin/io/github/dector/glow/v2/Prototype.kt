package io.github.dector.glow.v2

import io.github.dector.glow.v2.core.execute
import io.github.dector.glow.v2.dumbimpl.dumbDataProvider
import io.github.dector.glow.v2.dumbimpl.dumbDataPublisher
import io.github.dector.glow.v2.dumbimpl.dumbDataRenderer


fun main(args: Array<String>) {
    println("glow v2-pre-alpha\n")

    println(">> Simple app\n")

    val result = execute(dumbDataProvider,
            dumbDataRenderer,
            dumbDataPublisher)

    println("Glow finished with publishing result: ${result.publishResult::class.simpleName}")
}