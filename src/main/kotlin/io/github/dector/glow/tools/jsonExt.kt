package io.github.dector.glow.tools

import org.json.JSONObject

fun JSONObject.string(name: String, fallback: String = ""): String
        = if (has(name)) getString(name) else fallback

fun JSONObject.boolean(name: String, fallback: Boolean = false): Boolean
        = if (has(name)) getBoolean(name) else fallback