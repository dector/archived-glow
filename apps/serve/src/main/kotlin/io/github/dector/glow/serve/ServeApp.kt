package io.github.dector.glow.serve

import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.core.config.provideProjectConfig
import io.github.dector.glow.di.DI
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

            DI.provide(provideProjectConfig(projectDir, launchConfig))

            return ServeApp(Server())
        }
    }
}
