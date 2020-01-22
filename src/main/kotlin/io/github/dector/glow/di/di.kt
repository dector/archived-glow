package io.github.dector.glow.di

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Node
import io.github.dector.glow.core.components.DataPublisher
import io.github.dector.glow.core.components.FileDataPublisher
import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.PreprocessedDataPublisher
import io.github.dector.glow.core.config.NotesPluginConfig
import io.github.dector.glow.core.config.ProjectConfig
import io.github.dector.glow.core.config.RuntimeConfig
import io.github.dector.glow.core.config.provideProjectConfig
import io.github.dector.glow.core.logger.UILogger
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
import io.github.dector.glow.plugins.resources.ThemeAssetsPlugin
import io.github.dector.glow.ui.StdUiConsole
import io.github.dector.glow.ui.UiConsole
import org.koin.dsl.module
import java.io.File

fun appModule(projectDir: File) = module {
    single<GlowPipeline> {
        val logger = UILogger

        PluggablePipeline(
            NotesPlugin(get(), get(), get(), get(), logger),
            ThemeAssetsPlugin(get(), logger)
            //RssPlugin()
//                PagesPlugin(get(), get(), get(), logger),
//                StaticResourcesPlugin(get(), logger)
        )
    }
    single<GlowEngine> { PipelinedGlowEngine(get()) }

    single<UiConsole> { StdUiConsole() }

    single<MarkdownParser<Node>> { SimpleMarkdownParser() }
    single<HtmlRenderer> { HtmlRenderer.builder().build() }

    // mocks

    single<ProjectConfig> { provideProjectConfig(projectDir) }
    single<RuntimeConfig> { buildRuntimeConfig(projectConfig = get()) }
    single<DataPublisher> { PreprocessedDataPublisher(FileDataPublisher(get())) }

    // notes

    single<NotesPathResolver>() { NotesWebPathResolver(get()) }
    single<NotesDataProvider>() {
        val dir = get<ProjectConfig>().plugins.notes.sourceDir
        FileSystemNotesDataProvider(dir)
    }
    single<NotesDataRenderer>() { DefaultNotesDataRenderer(get(), get(), get()) }
}

private fun buildRuntimeConfig(projectConfig: ProjectConfig): RuntimeConfig {
    return RuntimeConfig(
        projectConfig = projectConfig,
        notes = NotesPluginConfig()
    )
}
