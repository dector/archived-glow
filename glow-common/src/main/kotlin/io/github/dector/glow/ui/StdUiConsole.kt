package io.github.dector.glow.ui

import java.io.PrintStream

class StdUiConsole(
    private val out: PrintStream = System.out
) : UiConsole {

    override var isEnabled: Boolean = true

    override fun print(text: String) {
        if (isEnabled) out.print(text)
    }

    override fun println(text: String) {
        if (isEnabled) out.println(text)
    }
}
