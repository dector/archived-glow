package space.dector.glow.config.project

import org.hjson.JsonArray
import org.hjson.JsonObject
import org.hjson.JsonValue
import space.dector.glow.config.LaunchConfig
import space.dector.ktx.div
import java.io.File
import java.nio.file.Paths

internal fun parseProjectConfig(file: File, context: ParsingContext): CProject {
    val content = buildString {
        appendln("{")
        appendln(file.readText())
        appendln("}")
    }

    return JsonValue
        .readHjson(content)
        .asObject()
        .asConfig(context)
}

private fun JsonObject.asConfig(context: ParsingContext) = CProject(
    glow = getObject("glow").asCGlow(),
    blog = getObject("blog").asCBlog(context),
    plugins = getObject("plugins").asCPlugins(context)
)

private fun JsonObject.asCGlow() = CGlow(
    config = getObject("config").asCConfig(),
    output = getObject("output").asCOutput(),
    assets = getObject("assets").asCAssets()
)

private fun JsonObject.asCConfig() = CConfig(
    version = get("version").asString()
)

private fun JsonObject.asCOutput() = COutput(
    overrideFiles = get("overrideFiles").asBoolean()
)

private fun JsonObject.asCAssets() = CAssets(
    targetPath = get("targetPath").asString()
        .let { Paths.get(it) }
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
    id = get("id").asString(),
    title = get("title").asString(),
    path = get("path").asString(),
    type = get("type").asString()
)

private fun JsonObject.asCFooter() = CFooter(
    author = get("author").asString(),
    year = get("year").toString(),
    licenseName = get("licenseName").asString(),
    licenseUrl = get("licenseUrl").asString()
)

private fun JsonObject.asCPlugins(context: ParsingContext) = CPlugins(
    notes = getObject("notes").asCNotesPlugin(context),
    domain = CDomainPlugin(
        cname = getObject("domain")?.get("domain")?.asString()
    )
)

private fun JsonObject.asCNotesPlugin(context: ParsingContext) = CNotesPlugin(
    sourceDir = context.dir / get("sourceDir").asString(),
    path = get("path").asString()
)

data class ParsingContext(
    val dir: File,
    val launchConfig: LaunchConfig
)

private fun JsonObject.getObject(name: String) = get(name).asObject()
