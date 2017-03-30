package io.github.dector.glow.builder.renderer.mustache

import com.samskivert.mustache.Mustache
import io.github.dector.glow.builder.models.PageModel
import io.github.dector.glow.builder.renderer.DefaultRenderFormatter
import io.github.dector.glow.builder.renderer.IRenderFormatter
import io.github.dector.glow.builder.renderer.IRenderer
import io.github.dector.glow.builder.renderer.PageType
import java.io.File
import java.io.Reader

class MustacheRenderer(
        private val templatesDir: File,
        val formatter: IRenderFormatter = DefaultRenderFormatter()) : IRenderer {

    private val mustache: Mustache.Compiler = Mustache.compiler()
            .withLoader { name -> templateFile(name).reader() }
            .escapeHTML(false)

    override fun render(pageType: PageType, model: PageModel): String = mustache
            .compile(templateReader(pageType))
            .execute(buildContext(model))

    private fun buildContext(pageModel: PageModel) = mapOf(
            "blogTitle" to pageModel.blog.title,
            "blogPosts" to pageModel.blog.posts,
            "hasBlogPosts" to pageModel.blog.posts.isNotEmpty(),
            "title" to pageModel.title,
            "tags" to pageModel.tags,
            "hasTags" to pageModel.tags.isNotEmpty(),
            "pubdate" to formatter.formatPubDate(pageModel.pubdate),
            "pubdateHint" to formatter.formatPubDateHint(pageModel.pubdate),
            "content" to pageModel.content)

    private fun templateReader(pageType: PageType): Reader
            = templateFile(pageType.filename).reader()

    private fun templateFile(name: String): File
            = File(templatesDir, "$name.mustache")
}