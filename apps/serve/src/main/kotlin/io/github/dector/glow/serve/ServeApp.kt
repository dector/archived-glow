package io.github.dector.glow.serve

import io.github.dector.glow.server.Server


class ServeApp(
    private val server: Server
) {

    fun execute() {
        server.run()
    }
}
