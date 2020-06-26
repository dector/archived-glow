package io.github.dector.glow.ui

import io.github.dector.glow.logger.setUiLoggerEnabled
import java.io.PrintStream

class StdUiConsole(
    private val out: PrintStream = System.out
) : UiConsole {

    override var isEnabled: Boolean = true
        set(value) {
            field = value
            setUiLoggerEnabled(value)
        }

    override fun print(text: String) {
        if (isEnabled) out.print(text)
    }

    override fun println(text: String) {
        if (isEnabled) out.println(text)
    }
}
