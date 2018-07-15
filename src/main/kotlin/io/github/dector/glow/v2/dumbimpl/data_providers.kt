package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.PathToProject
import io.github.dector.glow.v2.PostsDirName
import io.github.dector.glow.v2.core.DataProvider
import java.io.File


val dumbDataProvider: DataProvider = {
    val content = loadFiles(PathToProject)

    markdownFileParser(content)
}

private fun loadFiles(projectDir: String): List<String> {
    val dir = File(projectDir, PostsDirName)

    // TODO check errors

    return dir.listFiles { _, name -> name.endsWith(".md") }
            .map { it.readText() }
}