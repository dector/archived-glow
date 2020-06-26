package io.github.dector.glow.serve

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.di.configModule
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
            includeDrafts: Boolean
        ): ServeApp {
            val launchConfig = LaunchConfig(
                includeDrafts = includeDrafts
            )

            initApp(projectDir, launchConfig)

            return ServeApp(Server(launchConfig))
        }
    }
}

private fun initApp(projectDir: File, launchConfig: LaunchConfig) {
    //DI.init()
    DI.resetAction = {
        DI.init()
        DI.modify {
            it.modules(
                configModule(projectDir, launchConfig),
                appModule()
            )
        }
    }
    DI.reset()  // Will call init()
}
