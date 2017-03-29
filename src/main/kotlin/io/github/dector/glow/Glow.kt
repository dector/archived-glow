package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.renderer.IRenderer
import io.github.dector.glow.renderer.PageType
import io.github.dector.glow.renderer.mustache.MustacheRenderer
import io.github.dector.glow.tools.StopWatch
import org.json.JSONObject
import java.io.File
import java.io.FileFilter
import java.time.LocalDate

val CliHeader = """
      _  |  _
     (_| | (_) \/\/
      _|            v ${BuildConfig.VERSION}
"""

fun main(args: Array<String>) {
    val stopWatch = StopWatch().start()

    UiLogger.info(CliHeader)

    val opts = parseArguments(args = *args).let {
        parseConfigIfExists()?.copy(command = it.command) ?: it
    }

    if (!OptionsValidator().validate(opts))
        return

    when (opts.command) {
        GlowCommandInitOptions.Value -> {
            GlowProjectCreator(opts.commandInitOptions).process()
        }
        GlowCommandBuildOptions.Value -> Glow(opts.commandBuildOptions).process()
        else -> opts.logger().error("Command ${opts.command} not defined.")
    }

    UiLogger.info("\nFinished in ${stopWatch.stop().timeFormatted()}.")
}

private fun parseConfigIfExists(): GlowOptions? {
    val configFile = File("glow.json")

    if (!configFile.exists()) {
        UiLogger.info("[Preparation] Config file not found. CLI arguments will be used.")
        return null
    }

    UiLogger.info("[Preparation] Config file found. CLI arguments will be ignored.")

    val configJson = JSONObject(configFile.readText())

    // TODO check config version

    val commandInit = GlowCommandInitOptions(
            targetFolder = listOf(configJson.getString("output")))
    val commandBuild = GlowCommandBuildOptions(
            inputDir = File(configJson.string("input", "posts")),
            outputDir = File(configJson.string("output", "out")),
            themeDir = File(configJson.string("theme", "themes/simple")),
            clearOutputDir = configJson.boolean("clearOutput", false),
            blogTitle = configJson.string("title", "<: Unknown Blog :>"))

    return GlowOptions(
            commandInitOptions = commandInit,
            commandBuildOptions = commandBuild)
}

private fun parseArguments(baseOpts: GlowOptions = GlowOptions(), vararg args: String): GlowOptions {
    val commandMain = baseOpts.commandMainOptions
    val jc = JCommander(commandMain)

    val commandInit = baseOpts.commandInitOptions.also { jc.addCommand(GlowCommandInitOptions.Value, it) }
    val commandBuild = baseOpts.commandBuildOptions.also { jc.addCommand(GlowCommandBuildOptions.Value, it) }

    jc.parseWithoutValidation(*args)

    return baseOpts.copy(
            command = jc.parsedCommand,
            commandMainOptions = commandMain,
            commandInitOptions = commandInit,
            commandBuildOptions = commandBuild)
}

class Glow(private val opts: GlowCommandBuildOptions,
           val renderer: IRenderer = MustacheRenderer(opts.themeDir!!)) {

    private val logger = logger()

    private fun prepareDirs() {
        UiLogger.info("[Preparation] Checking output directories.")

        if (opts.clearOutputDir) {
            UiLogger.info("[Preparation] Removing existing output dir.")
            opts.outputDir?.deleteRecursively()
        }

        UiLogger.info("[Preparation] Creating output dir.")
        opts.outputDir?.mkdirs()
    }

    private fun listPostFiles() = opts.inputDir
            ?.listFiles(FileFilter { it.extension == "md" })
            ?: emptyArray()

    private fun copyAssets() {
        UiLogger.info("[Building] Copying theme assets to output.")

        File(opts.themeDir, "assets")
                .copyRecursively(File(opts.outputDir, "assets"))
    }

    private fun collectMeta(postFiles: Array<File>): List<PostMeta> {
        return postFiles
                .map(this::parsePost)
                .map { it.meta }
    }

    private fun writePage(file: File, html: String) {
        file.writeText(html)
    }

    private fun outputFileName(inputFile: File) = "${inputFile.nameWithoutExtension}.html"

    private fun outputFile(inputFile: File)
            = File(opts.outputDir, outputFileName(inputFile))

    private fun parsePost(file: File): ParsedPost {
        val parserOptions = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
        }

        val parser = Parser.builder(parserOptions).build()
        val renderer = HtmlRenderer.builder().build()

        val html = file.readText()

        val doc = parser.parse(html)
        val content = renderer.render(doc).trim()

        val yamlVisitor = AbstractYamlFrontMatterVisitor()
        yamlVisitor.visit(doc)

        val meta = PostMeta(
                title = yamlVisitor.data["title"]?.get(0) ?: "",
                tags = yamlVisitor.data["tags"]?.get(0)?.split(",")?.map(String::trim) ?: emptyList(),
                pubdate = dateTimeFromFilename(file.nameWithoutExtension),
                url = outputFileName(file),
                draft = yamlVisitor.data["draft"]?.get(0)?.toBoolean() ?: false,
                file = file)
        return ParsedPost(meta = meta, content = content)
    }

    private fun renderPost(file: File, data: GlobalData): String {
        val post = parsePost(file)
        val page = PageModel(
                title = post.meta.title,
                tags = post.meta.tags,
                pubdate = post.meta.pubdate,
                content = post.content,
                global = data)

        return render(PageType.Post, page)
    }

    private fun render(pageType: PageType, glowModel: PageModel): String
            = renderer.render(pageType, glowModel)

    private fun dateTimeFromFilename(name: String): LocalDate? {
        val parts = name.split("-")

        if (parts.size < 3)
            return null

        val year = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val day = parts[2].toIntOrNull()

        if (year == null || month == null || day == null)
            return null

        return LocalDate.of(year, month, day)
    }

    fun process() {
        prepareDirs()

        val postFiles = listPostFiles()

        UiLogger.info("[Building] ${postFiles.size} posts found.")

        UiLogger.info("[Building] Posts list.")
        val globalData = GlobalData(
                blogName = opts.blogTitle,
                posts = collectMeta(postFiles))

        UiLogger.info("[Building] Posts.")
        val filteredGlobal = globalData
                .copy(posts = globalData.posts.filter { !it.draft })
        filteredGlobal.posts
                .map { it.file }
                .forEach { writePage(outputFile(it), renderPost(it, filteredGlobal)) }

        writeArchivePage(filteredGlobal)
        writeIndexPage(filteredGlobal)

        copyAssets()

        UiLogger.info("[Building] ${globalData.posts.size} file(s) proceed.")
    }

    private fun writeArchivePage(data: GlobalData) {
        UiLogger.info("[Building] Archive page.")

        val archiveFile = File(opts.outputDir, "archive.html")
        val page = PageModel(
                global = data,
                title = "Archive",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Archive, page))
    }

    private fun writeIndexPage(data: GlobalData) {
        UiLogger.info("[Building] Index page.")

        val archiveFile = File(opts.outputDir, "index.html")
        val page = PageModel(
                global = data,
                title = "",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Index, page))
    }
}