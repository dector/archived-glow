package io.github.dector.glow.ui

import io.github.dector.glow.core.logger.UiConfig
import java.io.PrintStream

class StdUiConsole(
    private val out: PrintStream = System.out
) : UiConsole {

    private val isEnabled: Boolean get() = UiConfig.uiLoggerEnabled

    override fun print(text: String) {
        if (isEnabled) out.print(text)
    }

    override fun println(text: String) {
        if (isEnabled) out.println(text)
    }
}
