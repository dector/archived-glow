package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.BlogData
import io.github.dector.glow.v2.DataProvider
import io.github.dector.glow.v2.Page
import java.io.File
import java.io.FilenameFilter


val dumbDataProvider: DataProvider = {
    val content = loadFiles("v2/source/")

    dumbMdToHtmlConverter(content)
}

private fun loadFiles(projectDir: String): List<String> {
    val dir = File(projectDir, "posts")

    // TODO check errors

    return dir.listFiles(FilenameFilter { dir, name -> name.endsWith(".md") })
            .map { it.readText() }
}