package io.github.dector.glow.v2.mockimpl

import java.io.File


fun mockProjectsConfig() = ProjectConfig(
        input = InputConfig(
                staticFolder = File("v2/themes/dead-art/source"),
                pagesFolder = File("v2/src/pages"),
                notesFolder = File("v2/src/notes")
        ),
        output = OutputConfig(
                staticFolder = File("v2/out2"),
                pagesFolder = File("v2/out2"),
                notesFolder = File("v2/out2/notes"),
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
        val staticFolder: File,
        val pagesFolder: File,
        val notesFolder: File,
        val overrideFiles: Boolean = false
)

