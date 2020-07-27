package space.dector.glow.config.project

import org.hjson.JsonValue
import space.dector.glow.config.LaunchConfig
import java.io.File

internal fun parseProjectConfig(file: File, context: ParsingContext): ConfigWrapper {
    val content = buildString {
        appendln("{")
        appendln(file.readText())
        appendln("}")
    }

    val config = JsonValue
        .readHjson(content)
        .asObject()

    return ConfigWrapper(
        root = config,
        context = context
    )
}

data class ParsingContext(
    val dir: File,
    val launchConfig: LaunchConfig
)
