package io.github.dector.glow.core

import io.github.dector.glow.BuildSetup.DevMode
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.config.NavigationItem
import io.github.dector.glow.core.config.parseConfig
import java.io.File

@Deprecated("")
fun mockProjectsConfig() = ProdConfig

fun provideProjectConfig(file: File): Config =
    parseConfig(file.parentFile, file.readText())

@Deprecated("")
fun provideProjectConfig(): Config =
    provideProjectConfig(File("website/glow.yml"))

@Deprecated("")
fun provideBlogVM(config: Config) = BlogVM(   // Convert to VM later, provide setup model
    title = config.blog.title,
    navigation = config.blog.navigation.map {
        NavigationItem(path = it.path, title = it.title, type = it.type)
    },
    footer = FooterVM(
        author = config.blog.footer.author,
        year = config.blog.footer.year,
        licenseName = config.blog.footer.licenseName,
        licenseUrl = config.blog.footer.licenseUrl
    )
)

private val TestConfig = ProjectConfig(
    input = InputConfig(
        staticFolder = File("v2/themes/plain/source" + (if (DevMode) "-dev" else "")),
        pagesFolder = File("v2/src/pages")
    ),
    output = OutputConfig(
        staticFolder = File("v2/out2/")
    )
)

private val ProdConfig = ProjectConfig(
    input = InputConfig(
        staticFolder = File("v2/themes/dead-art/source" + (if (DevMode) "-dev" else "")),
        pagesFolder = File("website/src/pages")
    ),
    output = OutputConfig(
        staticFolder = File("v2/website-out/")
    )
)

@Deprecated("")
data class ProjectConfig(
    val input: InputConfig,
    val output: OutputConfig
)

@Deprecated("")
data class InputConfig(
    val staticFolder: File,
    val pagesFolder: File
)

@Deprecated("")
data class OutputConfig(
    val staticFolder: File
)

