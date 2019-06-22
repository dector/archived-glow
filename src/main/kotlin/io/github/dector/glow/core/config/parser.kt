package io.github.dector.glow.core.config

import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMapping
import com.amihaiemil.eoyaml.YamlSequence
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.div
import java.io.File

fun parseConfig(dir: File, content: String): Config = Yaml
    .createYamlInput(content)
    .readYamlMapping()
    .asConfig(dir)

private fun YamlMapping.asConfig(dir: File) = Config(
    glow = yamlMapping("glow").asCGlow(),
    blog = yamlMapping("blog").asCBlog(dir),
    plugins = yamlMapping("plugins").asCPlugins()
)

private fun YamlMapping.asCGlow() = CGlow(
    config = yamlMapping("config").asCConfig()
)

private fun YamlMapping.asCConfig() = CConfig(
    version = string("version")
)

private fun YamlMapping.asCBlog(dir: File) = CBlog(
    title = string("title"),
    navigation = yamlSequence("navigation")?.asCNavigationList() ?: emptyList(),
    footer = yamlMapping("footer").asCFooter(),
    sourceDir = dir / string("sourceDir")
)

private fun YamlSequence.asCNavigationList() =
    (0 until size())
        .map { yamlMapping(it) }
        .map { it.asCNavigation() }

private fun YamlMapping.asCNavigation() = CNavigation(
    title = string("title"),
    path = string("path"),
    type = NavItemType.from(string("type"))
)

private fun YamlMapping.asCFooter() = CFooter(
    author = string("author"),
    year = string("year"),
    licenseName = string("licenseName")
)

private fun YamlMapping.asCPlugins() = CPlugins(
    notes = yamlMapping("notes").asCNotesPlugin()
)

private fun YamlMapping.asCNotesPlugin() = CNotesPlugin(
    sourceDir = string("sourceDir")
)

fun main() {
    val file = File("website/glow.yml")
    println(parseConfig(file.parentFile, file.readText()))
}
