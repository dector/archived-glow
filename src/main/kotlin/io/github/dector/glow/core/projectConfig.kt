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
fun provideBlogVM() = BlogVM(   // Convert to VM later, provide setup model
    title = "Dead Art Space",
    navigation =
    provideProjectConfig().blog.navigation.map {
        NavigationItem(path = it.path, title = it.title, type = it.type)
    }

// FIXME
//                NavigationItem("/", "Home", Home),
//                NavigationItem("/notes", "Notes", Notes, visible = false),
//        NavigationItem("/", "Notes", Notes)//,
//                NavigationItem("https://forms.gle/PDqcYSiBY8Y4iVP5A", "Feedback", Feedback)
//                NavigationItem("/projects", "Projects", Projects),
//                NavigationItem("/about", "About", About)
    ,
    footer = FooterVM(
        author = "Dead Art Space",
        year = "2019",
        licenseName = "CC BY-SA 4.0",
        licenseUrl = "http://creativecommons.org/licenses/by-sa/4.0/"
    )
)

private val TestConfig = ProjectConfig(
    input = InputConfig(
        sourcesFolder = File("v2/src/"),
        staticFolder = File("v2/themes/plain/source" + (if (DevMode) "-dev" else "")),
        pagesFolder = File("v2/src/pages"),
        notesFolder = File("v2/src/notes")
    ),
    output = OutputConfig(
        outputFolder = File("v2/out2/"),

        staticFolder = File("v2/out2/"),
        notesPath = "/notes",
        overrideFiles = true
    ),
    navigation = provideBlogVM().navigation
)

private val ProdConfig = ProjectConfig(
    input = InputConfig(
        sourcesFolder = File("website/src/"),
        staticFolder = File("v2/themes/dead-art/source" + (if (DevMode) "-dev" else "")),
        pagesFolder = File("website/src/pages"),
        notesFolder = File("website/src/notes")
    ),
    output = OutputConfig(
        outputFolder = File("v2/website-out/"),

        staticFolder = File("v2/website-out/"),
        notesPath = "/notes",
        overrideFiles = true
    ),
    navigation = provideBlogVM().navigation
)

@Deprecated("")
data class ProjectConfig(
    val input: InputConfig,
    val output: OutputConfig,
    val navigation: List<NavigationItem>
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
    val sourcesFolder: File,
    val staticFolder: File,
    val pagesFolder: File,
    val notesFolder: File
)

data class OutputConfig(
    val outputFolder: File,
    val notesPath: String,

    val staticFolder: File,
    val overrideFiles: Boolean = false
)

