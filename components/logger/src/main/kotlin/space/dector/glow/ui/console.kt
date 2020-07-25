package space.dector.glow.ui

interface UiConsole {

    var isEnabled: Boolean

    fun print(text: String)
    fun println(text: String)

    fun print(any: Any) = print(any.toString())
    fun println(any: Any) = println(any.toString())

    fun println() = println("")

    companion object {
        val get by lazy(LazyThreadSafetyMode.NONE) { StdUiConsole() }
    }
}
