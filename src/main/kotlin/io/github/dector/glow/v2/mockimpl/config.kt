package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.mockimpl.BuildConfig.DevMode
import java.io.File


fun mockProjectsConfig() = ProjectConfig(
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
        navigation = listOf(
                NavigationItem("/", "Home"),
                NavigationItem("/notes", "Notes"),
                NavigationItem("/projects", "Projects"),
                NavigationItem("/about", "About")
        )
)

data class ProjectConfig(
        val input: InputConfig,
        val output: OutputConfig,
        val navigation: List<NavigationItem>
)

data class NavigationItem(
        val path: String,
        val title: String
)

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

