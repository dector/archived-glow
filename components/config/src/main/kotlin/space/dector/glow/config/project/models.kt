package space.dector.glow.config.project

import org.hjson.JsonObject


internal data class ConfigWrapper(
    val root: JsonObject,
    val context: ParsingContext
)
