package io.github.dector.glow.core

import io.github.dector.glow.BuildSetup.DevMode
import io.github.dector.glow.core.NavItemType.Notes
import java.io.File


fun mockProjectsConfig() = ProdConfig

fun provideBlogVM() = BlogVM(   // Convert to VM later, provide setup model
        title = "Dead Art Space",
        navigation = listOf(
                // FIXME
//                NavigationItem("/", "Home", Home),
//                NavigationItem("/notes", "Notes", Notes, visible = false),
                NavigationItem("/", "Notes", Notes)//,
//                NavigationItem("https://forms.gle/PDqcYSiBY8Y4iVP5A", "Feedback", Feedback)
//                NavigationItem("/projects", "Projects", Projects),
//                NavigationItem("/about", "About", About)
        ),
        footer = FooterVM(
                author = "Dead Art Space",
                year = "2019",
                licenseName = "CC BY-SA 4.0",
                licenseUrl = "http://creativecommons.org/licenses/by-sa/4.0/"
        )
)

private val TestConfig = ProjectConfig(
        input = InputConfig(
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

enum class NavItemType {
    Home, Notes, Projects, About,
    Feedback
}

data class InputConfig(
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

