package io.github.dector.glow.build

import io.github.dector.glow.CLI_HEADER
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.ui.UiConsole


class BuilderApp(
    private val ui: UiConsole,
    private val glow: GlowEngine
) {

    fun execute() {
        ui.println(CLI_HEADER)

        glow.execute()
    }
}
