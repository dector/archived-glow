package io.github.dector.glow.engine

data class NavigationItem(
    val path: String,
    val title: String,
    val type: NavItemType,
    val visible: Boolean = true
)

enum class NavItemType(val id: String) {
    Home("home"),
    Notes("notes"),
    Projects("projects"),
    About("about"),
    Feedback("feedback");

    companion object {

        fun from(id: String) = values().first { it.id == id }
    }
}
