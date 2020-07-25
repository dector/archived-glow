package space.dector.glow.server

import io.javalin.Javalin
import space.dector.glow.di.DI
import space.dector.glow.di.get
import space.dector.glow.engine.RenderedWebPage

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
