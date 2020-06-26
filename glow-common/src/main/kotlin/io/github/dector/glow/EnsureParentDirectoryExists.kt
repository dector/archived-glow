package io.github.dector.glow

import java.io.File

fun File.ensureParentDirectoryExists() = apply { parentFile.mkdirs() }
