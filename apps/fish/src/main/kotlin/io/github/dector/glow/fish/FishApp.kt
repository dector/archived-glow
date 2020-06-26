package io.github.dector.glow.fish

import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.ui.UiConsole


class FishApp private constructor(
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

    companion object {
        fun create(): FishApp = FishApp(
            DI.get()
        )
    }
}
