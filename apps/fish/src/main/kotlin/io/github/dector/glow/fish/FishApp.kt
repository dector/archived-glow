package io.github.dector.glow.fish

import io.github.dector.glow.ui.UiConsole


class FishApp(
    private val ui: UiConsole
) {

    private val fish = """
          /`·.¸
         /¸...¸`:·
     ¸.·´  ¸   `·.¸.·´)
    : © ):´;      ¸  {
     `·.¸ `·  ¸.·´\`·¸)
         `\\´´\¸.·´     Hi, there!"""

    fun execute() {
        ui.println(fish)
    }
}
