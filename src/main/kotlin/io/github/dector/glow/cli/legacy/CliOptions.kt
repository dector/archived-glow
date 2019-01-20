package io.github.dector.glow.cli.legacy

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.beust.jcommander.converters.FileConverter
import io.github.dector.glow.logger.logger
import io.github.dector.glow.tools.assert
import io.github.dector.glow.tools.isTrue
import java.io.File

data class GlowOptions(
        val command: String = "",
        val commandMainOptions: GlowCommandMainOptions = GlowCommandMainOptions(),
        val commandInitOptions: GlowCommandInitOptions = GlowCommandInitOptions(),
        val commandBuildOptions: GlowCommandBuildOptions = GlowCommandBuildOptions())

data class GlowCommandMainOptions(
        @Parameter(names = ["-h", "--help"], description = "Display this usage info and exit", help = true)
        var help: Boolean = false,

        @Parameter(names = ["-q", "--quiet"], description = "Don't print anything")
        var quiet: Boolean = false,

        var usageText: String? = null) {

    // verbose

}

@Parameters(separators = "=", commandDescription = "Initialize new project")
data class GlowCommandInitOptions(
        @Parameter()
        var targetFolder: List<String> = emptyList()) {

    companion object {

        val Value = "init"
    }
}

@Parameters(separators = "=", commandDescription = "Build project")
data class GlowCommandBuildOptions(
        @Parameter(names = ["-i", "--input"], converter = FileConverter::class)
        var inputDir: File? = null,

        @Parameter(names = ["-o", "--output"], converter = FileConverter::class)
        var outputDir: File? = null,

        @Parameter(names = ["-t", "--theme"], converter = FileConverter::class)
        var themeDir: File? = null,

        @Parameter(names = ["--clear-output"])
        var clearOutputDir: Boolean = false,

        @Parameter(names = ["--title"])
        var blogTitle: String = "") {

    companion object {

        val Value = "build"
    }
}

class OptionsValidator {

    private val logger = logger()

    fun validateInitCommand(opts: GlowCommandInitOptions): Boolean = validateNewProjectTargetDir(opts.targetFolder)

    fun validateBuildCommand(opts: GlowCommandBuildOptions): Boolean = validateInputDir(opts.inputDir)
            && validateOutputDir(opts.outputDir, opts.clearOutputDir)
            && validateThemeDir(opts.themeDir)

    private fun validateNewProjectTargetDir(dirs: List<String>): Boolean {
        assert("Target dir should be set", logger) { dirs.isNotEmpty() } ?: return false
        assert("Only one target dir should be set", logger) { dirs.size == 1 } ?: return false

        File(dirs[0]).also {
            assert("Target dir shouldn't exists or must be empty", logger) { !it.exists() || (it.isDirectory && it.listFiles().isEmpty()) }
                    ?: return false
        }

        return true
    }

    private fun validateInputDir(dir: File?): Boolean {
        assert("Input dir should be set", logger) { dir != null } ?: return false
        assert("Input dir should be directory", logger) { dir?.isDirectory } ?: return false

        return true
    }

    private fun validateOutputDir(dir: File?, canExist: Boolean): Boolean {
        assert("Output dir should be set", logger) { dir != null } ?: return false

        if (!canExist) {
            assert("Output dir should not exist", logger) {
                !(dir?.exists() ?: false) || dir?.listFiles()?.isEmpty().isTrue()
            }
                    ?: return false
        }

        return true
    }

    private fun validateThemeDir(dir: File?): Boolean {
        assert("Theme dir should be set", logger) { dir != null } ?: return false

        return true
    }
}