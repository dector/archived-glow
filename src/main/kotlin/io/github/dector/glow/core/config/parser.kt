package io.github.dector.glow.core.config

import io.github.dector.glow.div
import org.hjson.JsonArray
import org.hjson.JsonObject
import org.hjson.JsonValue
import java.io.File
import java.nio.file.Paths

fun parseConfig(file: File, context: ParsingContext): ProjectConfig = JsonValue
    .readHjson(file.readText())
    .asObject()
    .asConfig(context)

private fun JsonObject.asConfig(context: ParsingContext) = ProjectConfig(
    projectDir = context.dir.toPath(),

    glow = getObject("glow").asCGlow(),
    blog = getObject("blog").asCBlog(context),
    plugins = getObject("plugins").asCPlugins(context)
)

private fun JsonObject.asCGlow() = CGlow(
    config = getObject("config").asCConfig(),
    output = getObject("output").asCOutput(),
    assets = getObject("assets").asCAssets(),
    _theme = get("_theme").asString()
)

private fun JsonObject.asCConfig() = CConfig(
    version = get("version").toString()
)

private fun JsonObject.asCOutput() = COutput(
    overrideFiles = get("overrideFiles").asBoolean()
)

private fun JsonObject.asCAssets() = CAssets(
    targetPath = get("targetPath").asString().let(Paths::get)
)

private fun JsonObject.asCBlog(context: ParsingContext) = CBlog(
    title = get("title").asString(),
    navigation = get("navigation").asArray().asCNavigationList(),
    footer = getObject("footer").asCFooter(),
    sourceDir = context.dir / get("sourceDir").asString(),
    outputDir = context.dir / get("outputDir").asString()
)

private fun JsonArray.asCNavigationList() =
    (0 until size())
        .map { get(it).asObject() }
        .map { it.asCNavigation() }

private fun JsonObject.asCNavigation() = CNavigation(
    title = get("title").asString(),
    path = get("path").asString(),
    type = NavItemType.from(get("type").asString())
)

private fun JsonObject.asCFooter() = CFooter(
    author = get("author").asString(),
    year = get("year").toString(),
    licenseName = get("licenseName").asString(),
    licenseUrl = get("licenseUrl").asString()
)

private fun JsonObject.asCPlugins(context: ParsingContext) = CPlugins(
    notes = getObject("notes").asCNotesPlugin(context)
)

private fun JsonObject.asCNotesPlugin(context: ParsingContext) = CNotesPlugin(
    sourceDir = context.dir / get("sourceDir").asString(),
    path = get("path").asString()
)

fun findConfig(dir: File): File {
    require(dir.isDirectory)

    val candidates = dir.listFiles()!!
        .filter { it.extension == "glow" }

    require(candidates.isNotEmpty()) { "No `.glow` file found in ${dir.absolutePath}" }
    require(candidates.size == 1)

    return candidates.first()
}

data class ParsingContext(
    val dir: File
)

fun main() {
    val dir = File("website")

    val context = ParsingContext(
        dir = dir
    )

    val configFile = findConfig(dir)
    val config = parseConfig(configFile, context)

    println(config)
}

private fun JsonObject.getObject(name: String) = get(name).asObject()
