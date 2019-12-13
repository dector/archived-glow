package io.github.dector.glow.plugins.pages

import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.legacy.ProjectConfig
import io.github.dector.glow.core.path.cleanupTitleForWebPath


interface PagesPathResolver {

    fun resolve(page: Page2): WebPagePath
}

class PagesWebPathResolver(
        private val config: ProjectConfig
) : PagesPathResolver {

    override fun resolve(page: Page2): WebPagePath {
        fun pageInstanceName() = run {
            val path = config.input.pagesFolder.toURI()
                    .relativize(page.sourceFile.toURI())

            path.resolve(page.title.cleanupTitleForWebPath())
        }

        val instancePath = when {
            page.sourceFile.nameWithoutExtension == "index" -> "index.html"
            page.isSection -> "${pageInstanceName()}/index.html"
            else -> "${pageInstanceName()}.html"
        }

        return WebPagePath(instancePath)
    }
}
