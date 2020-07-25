package space.dector.ktx


inline fun <reified T> T.applyIf(predicate: Boolean, block: T.() -> Unit): T =
    if (predicate) apply(block) else this

inline fun <reified T> T.runIf(predicate: Boolean, block: T.() -> T): T =
    if (predicate) run(block) else this
