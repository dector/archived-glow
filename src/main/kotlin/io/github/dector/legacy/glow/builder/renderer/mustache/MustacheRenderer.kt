package io.github.dector.legacy.glow.builder.renderer.mustache

import com.samskivert.mustache.Mustache
import io.github.dector.glow.core.utils.another
import io.github.dector.legacy.glow.builder.models.PageData
import io.github.dector.legacy.glow.builder.models.PostMeta
import io.github.dector.legacy.glow.builder.renderer.DefaultRenderFormatter
import io.github.dector.legacy.glow.builder.renderer.IRenderFormatter
import io.github.dector.legacy.glow.builder.renderer.IRenderer
import io.github.dector.legacy.glow.builder.renderer.PageType
import java.io.File
import java.io.Reader

class MustacheRenderer(
        private val templatesDir: File) : IRenderer {

    private val vmBuilder = VMBuilder()

    private val mustache: Mustache.Compiler = Mustache.compiler()
            .withLoader { name -> templateFile(name).reader() }
            .escapeHTML(false)

    override fun render(pageType: PageType, data: PageData): String = mustache
            .compile(templateReader(pageType))
            .execute(vmBuilder[pageType, data])

    private fun templateReader(pageType: PageType): Reader = templateFile(pageType.filename).reader()

    private fun templateFile(name: String): File = File(templatesDir, "$name.mustache")
}

private class VMBuilder(
        private val formatter: IRenderFormatter = DefaultRenderFormatter()) {

    operator fun get(pageType: PageType, page: PageData): BaseVM = when (pageType) {
        PageType.Index -> buildIndexVM(page)
        PageType.Archive -> buildArchiveVM(page)
        PageType.Post -> buildPostVM(page)
    }

    private fun buildBaseVM(page: PageData) = BaseVM(
            blogTitle = page.blog.title,
            title = page.title)

    private fun buildIndexVM(page: PageData) = IndexVM(
            baseVM = buildBaseVM(page),
            blogPosts = page.blog.posts.asVM())

    private fun buildArchiveVM(page: PageData) = ArchiveVM(
            baseVM = buildBaseVM(page),
            blogPosts = page.blog.posts.asVM())

    private fun buildPostVM(page: PageData) = PostVM(
            baseVM = buildBaseVM(page),
            tags = page.tags,
            pubdate = formatter.formatPubDate(page.pubDate),
            pubdateHint = formatter.formatPubDateHint(page.pubDate),
            content = page.content,
            prevPost = page.prev?.asVM(),
            nextPost = page.next?.asVM())

    private fun List<PostMeta>.asVM() = map { it.asVM() }

    private fun PostMeta.asVM() = PostInfoVM(
            title = title,
            url = url,
            pubdate = formatter.formatPubDate(pubDate))
}

private open class BaseVM(
        var blogTitle: String,
        var title: String) {

    constructor(it: BaseVM) : this(
            blogTitle = it.blogTitle,
            title = it.title)
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
        var content: String,
        var prevPost: PostInfoVM?,
        var nextPost: PostInfoVM?) : BaseVM(baseVM) {

    val hasTags by another { tags.isNotEmpty() }
    val hasPrevPost by another { prevPost != null }
    val hasNextPost by another { nextPost != null }
}