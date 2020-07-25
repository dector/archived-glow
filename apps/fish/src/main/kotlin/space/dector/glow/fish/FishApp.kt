package space.dector.glow.fish

import space.dector.glow.ui.UiConsole


/**
 * Draw fish and exit.
 */
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
            UiConsole.get
        )
    }
}
