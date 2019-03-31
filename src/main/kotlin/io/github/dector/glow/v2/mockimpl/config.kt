package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.mockimpl.BuildConfig.DevMode
import java.io.File


fun mockProjectsConfig() = ProjectConfig(
        input = InputConfig(
                staticFolder = File("v2/themes/dead-art/source" + (if (DevMode) "-dev" else "")),
                pagesFolder = File("v2/src/pages"),
                notesFolder = File("v2/src/notes")
        ),
        output = OutputConfig(
                outputFolder = File("v2/out2"),

                staticFolder = File("v2/out2"),
                notesPath = "/notes",
                overrideFiles = true
        )
)

data class ProjectConfig(
        val input: InputConfig,
        val output: OutputConfig
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

