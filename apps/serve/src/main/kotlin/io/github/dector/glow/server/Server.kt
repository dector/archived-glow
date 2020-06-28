package io.github.dector.glow.server

import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.engine.RenderedWebPage
import io.javalin.Javalin

class Server(
    pagesStorage: MutableSet<RenderedWebPage>
) {

    private val app: Javalin = Javalin.create { config ->
        config.showJavalinBanner = false
    }

    private val rootHandler = RootHandler(DI.get(), pagesStorage)

    fun run() {
        startServer(PORT)
    }

    private fun startServer(port: Int) {
        println("Running server on port $port... ")

        app.get("/*", rootHandler)
        app.start(port)

        println("Started on: http://localhost:$port")
    }
}

private const val PORT = 9217
