package io.github.dector.glow.builder.parser

import java.io.File

interface IPostParser {

    fun parse(file: File): ParsedPost
}