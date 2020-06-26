package io.github.dector.glow.build

import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.appModule
import io.github.dector.glow.di.get
import io.github.dector.glow.ui.UiConsole
import java.io.File


class BuilderApp private constructor(
    private val ui: UiConsole,
    private val glow: GlowEngine
) {

    fun execute() {
        ui.println(CLI_HEADER)

        glow.execute()
    }

    companion object {
        fun create(
            projectDir: File,
            quiet: Boolean
        ): BuilderApp {
            initApp(projectDir)

            val ui = DI.get<UiConsole>()
            if (quiet) ui.isEnabled = false

            return BuilderApp(ui, DI.get())
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
