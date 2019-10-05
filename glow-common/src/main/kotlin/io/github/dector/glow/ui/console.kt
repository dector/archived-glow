package io.github.dector.glow.ui

interface UiConsole {

    fun print(text: String)
    fun println(text: String)

    fun print(any: Any) = print(any.toString())
    fun println(any: Any) = println(any.toString())
}
