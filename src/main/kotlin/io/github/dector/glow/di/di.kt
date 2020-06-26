package io.github.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.config.LaunchConfig
import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.FileDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.core.config.provideProjectConfig
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.parser.SimpleMarkdownParser
import io.github.dector.glow.pipeline.GlowPipeline
import io.github.dector.glow.pipeline.PipelinedGlowEngine
import io.github.dector.glow.pipeline.PluggablePipeline
import io.github.dector.glow.plugins.notes.DefaultNotesDataRenderer
import io.github.dector.glow.plugins.notes.FileSystemNotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataProvider
import io.github.dector.glow.plugins.notes.NotesDataRenderer
import io.github.dector.glow.plugins.notes.NotesPathResolver
import io.github.dector.glow.plugins.notes.NotesPlugin
import io.github.dector.glow.plugins.notes.NotesWebPathResolver
import io.github.dector.glow.plugins.resources.AssetsPlugin2
import io.github.dector.glow.plugins.resources.ThemeAssetsPlugin
import org.koin.dsl.module
import java.io.File

fun appModule(projectDir: File, launchConfig: LaunchConfig) = module {
    single<GlowPipeline> {
        PluggablePipeline(
            NotesPlugin(get(), get(), get(), get(), get()),
            AssetsPlugin2(get(), get()),
            ThemeAssetsPlugin(get())
            //RssPlugin()
//                PagesPlugin(get(), get(), get(), logger),
//                StaticResourcesPlugin(get(), logger)
        )
    }
    single<GlowEngine> { PipelinedGlowEngine(get()) }

    single<MarkdownParser<Node>> { SimpleMarkdownParser() }
    single<HtmlRenderer> { HtmlRenderer.builder().build() }

    // mocks

    single<RuntimeConfig> { provideProjectConfig(projectDir, launchConfig) }
    single<NotesPluginConfig> { NotesPluginConfig() }
    single<DataPublisher> { PreprocessedDataPublisher(FileDataPublisher(get())) }

    // notes

    single<NotesPathResolver>() { NotesWebPathResolver(get()) }
    single<NotesDataProvider>() {
        val dir = get<RuntimeConfig>().glow.notes.sourceDir.toFile()
        FileSystemNotesDataProvider(dir)
    }
    single<NotesDataRenderer>() { DefaultNotesDataRenderer(get(), get(), get()) }
}
