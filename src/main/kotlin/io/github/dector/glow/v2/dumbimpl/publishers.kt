package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.DataPublisher
import io.github.dector.glow.v2.core.PublishResult
import io.github.dector.glow.v2.models.ProcessedPage
import java.io.File


val dumbDataPublisher: DataPublisher = { data ->
    val buildDir = File("v2/out").apply {
        deleteRecursively()
        mkdir()
    }

    fun writeToFile(page: ProcessedPage) {
        val filename = "${page.path}.html"
        val file = File(buildDir, filename)
        file.parentFile.mkdirs()

        file.writeText(page.content)
    }

    data.pages.forEach (::writeToFile)
    data.indexPages.forEach (::writeToFile)
    data.tagPages.forEach (::writeToFile)

    PublishResult.Success()
}