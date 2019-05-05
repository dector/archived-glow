package io.github.dector.legacy.glow.builder.parser

import java.io.File

interface IPostParser {

    fun parse(file: File): ParsedPost
}