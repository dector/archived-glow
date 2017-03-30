package io.github.dector.glow.builder.parser

import io.github.dector.glow.builder.models.ParsedPost
import java.io.File

interface IPostParser {

    fun parse(file: File): ParsedPost
}