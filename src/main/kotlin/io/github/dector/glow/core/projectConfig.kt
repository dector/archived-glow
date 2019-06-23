package io.github.dector.glow.core

import io.github.dector.glow.BuildSetup.DevMode
import io.github.dector.glow.core.config.Config
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
        staticFolder = File("v2/out2/"),
        overrideFiles = true
    )
)

private val ProdConfig = ProjectConfig(
    input = InputConfig(
        staticFolder = File("v2/themes/dead-art/source" + (if (DevMode) "-dev" else "")),
        pagesFolder = File("website/src/pages")
    ),
    output = OutputConfig(
        staticFolder = File("v2/website-out/"),
        overrideFiles = true
    )
)

@Deprecated("")
data class ProjectConfig(
    val input: InputConfig,
    val output: OutputConfig
)

data class NavigationItem(
    val path: String,
    val title: String,
    val type: NavItemType,
    val visible: Boolean = true
)

enum class NavItemType(val id: String) {
    Home("home"),
    Notes("notes"),
    Projects("projects"),
    About("about"),
    Feedback("feedback");

    companion object {

        fun from(id: String) = values().first { it.id == id }
    }
}

data class InputConfig(
    val staticFolder: File,
    val pagesFolder: File
)

data class OutputConfig(
    val staticFolder: File,
    val overrideFiles: Boolean = false
)

