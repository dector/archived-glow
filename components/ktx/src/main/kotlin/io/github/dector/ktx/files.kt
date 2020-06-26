package io.github.dector.ktx

import java.io.File


operator fun File.div(path: String) = resolve(path)

fun File.ensureParentDirectoryExists() = apply { parentFile.mkdirs() }
