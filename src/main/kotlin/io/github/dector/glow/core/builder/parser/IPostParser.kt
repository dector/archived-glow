package io.github.dector.glow.core.builder.parser

import java.io.File

interface IPostParser {

    fun parse(file: File): ParsedPost
}