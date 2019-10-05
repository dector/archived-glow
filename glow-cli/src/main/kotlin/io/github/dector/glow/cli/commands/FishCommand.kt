package io.github.dector.glow.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import io.github.dector.glow.ui.UiConsole

class FishCommand(private val console: UiConsole) : CliktCommand(name = "fish") {

    private val fish = """
          /`·.¸
         /¸...¸`:·
     ¸.·´  ¸   `·.¸.·´)
    : © ):´;      ¸  {
     `·.¸ `·  ¸.·´\`·¸)
         `\\´´\¸.·´     Hi, there!"""

    override fun run() {
        console.println(fish)
    }
}
