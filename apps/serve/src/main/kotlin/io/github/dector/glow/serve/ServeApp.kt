package io.github.dector.glow.serve

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.server.Server
import java.io.File


class ServeApp private constructor(
    private val server: Server
) {

    fun execute() {
        server.run()
    }

    companion object {
        fun create(
            projectDir: File,
            launchConfig: LaunchConfig
        ): ServeApp {
            initApp(projectDir)

            return ServeApp(Server(launchConfig))
        }
    }
}

private fun initApp(projectDir: File) {
    //DI.init()
    DI.resetAction = {
        DI.init()
        DI.modify {
            it.modules(appModule(projectDir))
        }
    }
    DI.reset()  // Will call init()
}
