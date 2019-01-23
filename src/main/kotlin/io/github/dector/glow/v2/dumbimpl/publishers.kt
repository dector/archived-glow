package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.DistDirName
import io.github.dector.glow.v2.PathToProject
import io.github.dector.glow.v2.core.*
import java.io.File


val dumbDataPublisher = object : DataPublisher {

    override fun publish(webPage: WebPage) = error("")
    override fun publishPage(page: RenderedPage) = error("")
    override fun publishNote(note: RenderedNote) = error("")
    override fun publishNotesIndex(htmlContent: String) = error("")

    override fun publish(data: ProcessedData): PublishResult {
        val buildDir = File("v2/out").apply {
            listFiles().forEach { it.deleteRecursively() }
        }

        fun writeToFile(page: ProcessedPage) {
            val filename = page.path
            val file = File(buildDir, filename)
            file.parentFile.mkdirs()

            file.writeText(page.content)
        }

        data.postPages.forEach(::writeToFile)
        data.indexPages.forEach(::writeToFile)
        data.tagPages.forEach(::writeToFile)

        File(PathToProject, DistDirName)
                .copyRecursively(File(buildDir, DistDirName))

        return PublishResult.Success()
    }
}