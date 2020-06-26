package io.github.dector.glow

import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.NavigationItem
import io.github.dector.glow.engine.WebPagePath


fun BlogVM.detectNavItem(path: WebPagePath): NavigationItem? {
    val exactFind = navigation.find { it.path == path.value }
    if (exactFind != null)
        return exactFind

    val folderPath = path.pathToFolder()

    return navigation
        .sortedByDescending { it.path.length }
        .find { folderPath.startsWith(it.path) }
}

fun WebPagePath.parentFolder() = WebPagePath(pathToFolder())

fun WebPagePath.pathToFolder(): String {
    val latestCutIndex = value.lastIndexOf('/')
    val parentPath = if (latestCutIndex > 0)
        value.substring(0, latestCutIndex)
    else ""

    return if (parentPath.startsWith("/")) parentPath else "/$parentPath"
}
