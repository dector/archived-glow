package io.github.dector.glow.v2.mockimpl

import java.io.File


fun mockProjectsConfig() = ProjectConfig(
        input = InputConfig(
                pagesFolder = File("v2/src/pages")
        ),
        output = OutputConfig(
                pagesFolder = File("v2/out2/pages")
        )
)

data class ProjectConfig(
        val input: InputConfig,
        val output: OutputConfig
)

data class InputConfig(
        val pagesFolder: File
)


data class OutputConfig(
        val pagesFolder: File
)

