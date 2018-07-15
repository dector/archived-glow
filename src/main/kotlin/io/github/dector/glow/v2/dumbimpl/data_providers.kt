package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.DataProvider
import java.io.File
import java.io.FilenameFilter


val dumbDataProvider: DataProvider = {
    val content = loadFiles("v2/source/")

    // Save md, don't convert to html
    mdFileParser(content)
}

private fun loadFiles(projectDir: String): List<String> {
    val dir = File(projectDir, "posts")

    // TODO check errors

    return dir.listFiles(FilenameFilter { dir, name -> name.endsWith(".md") })
            .map { it.readText() }
}