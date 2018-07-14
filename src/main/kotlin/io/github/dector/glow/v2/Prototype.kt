package io.github.dector.glow.v2

import io.github.dector.glow.v2.dumbimpl.dumbDataProvider
import io.github.dector.glow.v2.dumbimpl.dumbDataPublisher
import io.github.dector.glow.v2.dumbimpl.dumbDataRenderer
import io.github.dector.glow.v2.dumbimpl.dumbMdToHtmlConverter


fun main(args: Array<String>) {
    println("Prototyping v2\n")

    println("Running dumb flow\n")

    val result = execute(dumbDataProvider,
            dumbDataRenderer,
            dumbDataPublisher)

    println("Glow finished with publishing result: ${result.publishResult}")
}