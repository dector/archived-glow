package space.dector.ktx

import space.dector.glow.ui.UiConsole


fun progress(
    message: String,
    ui: UiConsole = UiConsole.get,
    action: StepperContext.() -> Unit
) {
    ui.print(message)
    StepperContext(ui).apply(action)
    ui.println()
}

class StepperContext(
    private val ui: UiConsole
) {

    private fun <T : Any?> step(action: () -> T): T {
        ui.print(".")
        return action()
    }

    operator fun <T : Any?> (() -> T).unaryPlus(): T = step(this)
}
