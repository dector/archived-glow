package io.github.dector.glow.builder.renderer.mustache

import com.samskivert.mustache.Mustache
import io.github.dector.glow.builder.models.PageModel
import io.github.dector.glow.builder.models.PostMeta
import io.github.dector.glow.builder.renderer.DefaultRenderFormatter
import io.github.dector.glow.builder.renderer.IRenderFormatter
import io.github.dector.glow.builder.renderer.IRenderer
import io.github.dector.glow.builder.renderer.PageType
import io.github.dector.glow.tools.another
import java.io.File
import java.io.Reader

class MustacheRenderer(
        private val templatesDir: File) : IRenderer {

    private val vmBuilder = VMBuilder()

    private val mustache: Mustache.Compiler = Mustache.compiler()
            .withLoader { name -> templateFile(name).reader() }
            .escapeHTML(false)

    override fun render(pageType: PageType, model: PageModel): String = mustache
            .compile(templateReader(pageType))
            .execute(vmBuilder[pageType, model])

    private fun templateReader(pageType: PageType): Reader
            = templateFile(pageType.filename).reader()

    private fun templateFile(name: String): File
            = File(templatesDir, "$name.mustache")
}

private class VMBuilder(
        private val formatter: IRenderFormatter = DefaultRenderFormatter()) {

    operator fun get(pageType: PageType, page: PageModel): BaseVM = when(pageType) {
        PageType.Index -> buildIndexVM(page)
        PageType.Archive -> buildArchiveVM(page)
        PageType.Post -> buildPostVM(page)
    }

    private fun buildBaseVM(page: PageModel) = BaseVM(
            blogTitle   = page.blog.title,
            title       = page.title)

    private fun buildIndexVM(page: PageModel) = IndexVM(
            baseVM      = buildBaseVM(page),
            blogPosts   = page.blog.posts.asVM())

    private fun buildArchiveVM(page: PageModel) = ArchiveVM(
            baseVM      = buildBaseVM(page),
            blogPosts   = page.blog.posts.asVM())

    private fun buildPostVM(page: PageModel) = PostVM(
            baseVM      = buildBaseVM(page),
            tags        = page.tags,
            pubdate     = formatter.formatPubDate(page.pubDate),
            pubdateHint = formatter.formatPubDateHint(page.pubDate),
            content     = page.content)

    private fun List<PostMeta>.asVM() = map { it.asVM() }

    private fun PostMeta.asVM() = PostInfoVM(
            title   = title,
            url     = url,
            pubdate = formatter.formatPubDate(pubDate))
}

private open class BaseVM(
        var blogTitle: String,
        var title: String) {

    constructor(it: BaseVM) : this(
            blogTitle   = it.blogTitle,
            title       = it.title)
}

private class PostInfoVM(
        var title: String,
        var url: String,
        var pubdate: String)

private class IndexVM(
        baseVM: BaseVM,
        var blogPosts: List<PostInfoVM>) : BaseVM(baseVM) {

    val hasBlogPosts by another { blogPosts.isNotEmpty() }
}

private class ArchiveVM(
        baseVM: BaseVM,
        var blogPosts: List<PostInfoVM>) : BaseVM(baseVM) {

    val hasBlogPosts by another { blogPosts.isNotEmpty() }
}

private class PostVM(
        baseVM: BaseVM,
        var tags: List<String>,
        var pubdate: String,
        var pubdateHint: String,
        var content: String): BaseVM(baseVM) {

    // Observable needed
    val hasTags by another { tags.isNotEmpty() }
}