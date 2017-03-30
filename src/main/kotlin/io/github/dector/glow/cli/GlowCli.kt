package io.github.dector.glow.cli

import com.beust.jcommander.JCommander
import io.github.dector.glow.BuildConfig
import io.github.dector.glow.builder.GlowBuilder
import io.github.dector.glow.creator.GlowProjectCreator
import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.logger.logger
import io.github.dector.glow.tools.StopWatch
import io.github.dector.glow.tools.boolean
import io.github.dector.glow.tools.string
import org.json.JSONObject
import java.io.File
import kotlin.system.exitProcess

class GlowCli {

    private val CONFIG_VERSION_CURRENT = "1"

    private val CLI_HEADER = """
      _  |  _
     (_| | (_) \/\/
      _|            v ${BuildConfig.VERSION}
"""

    private val logger = logger()

    fun execute(vararg args: String) {
        val stopWatch = StopWatch().start()

        UiLogger.info(CLI_HEADER)

        val opts = parseArguments(args = *args)
        if (!validateAndProcessCommand(opts)) {
            UiLogger.info("\nFailed after ${stopWatch.stop().timeFormatted()}.")
            exitProcess(1)
        }

        UiLogger.info("\nFinished in ${stopWatch.stop().timeFormatted()}.")
    }

    private fun parseArguments(baseOpts: GlowOptions = GlowOptions(), vararg args: String): GlowOptions {
        val commandMain = baseOpts.commandMainOptions
        val jc = JCommander(commandMain)

        val commandInit = baseOpts.commandInitOptions.also { jc.addCommand(GlowCommandInitOptions.Value, it) }
        val commandBuild = baseOpts.commandBuildOptions.also { jc.addCommand(GlowCommandBuildOptions.Value, it) }

        jc.parseWithoutValidation(*args)

        return baseOpts.copy(
                command = jc.parsedCommand,
                commandMainOptions = commandMain,
                commandInitOptions = commandInit,
                commandBuildOptions = commandBuild)
    }

    private fun validateAndProcessCommand(opts: GlowOptions): Boolean {
        when (opts.command) {
            GlowCommandInitOptions.Value -> {
                if (OptionsValidator().validateInitCommand(opts.commandInitOptions))
                    GlowProjectCreator(opts.commandInitOptions).process()
                else return false
            }
            GlowCommandBuildOptions.Value -> {
                val (configBuildOpts, configVersion) = parseConfigIfExists()

                if (configVersion != CONFIG_VERSION_CURRENT) {
                    logger.error("Config version `$configVersion` is not supported. Actual version is `$CONFIG_VERSION_CURRENT`")
                    return false
                }

                val buildOpts = if (configBuildOpts != null) {
                    UiLogger.info("[Preparation] Config file not found. CLI arguments will be used...")
                    configBuildOpts
                } else {
                    UiLogger.info("[Preparation] Config file found. CLI arguments will be ignored...")
                    opts.commandBuildOptions
                }

                if (OptionsValidator().validateBuildCommand(buildOpts))
                    return GlowBuilder(buildOpts).process()
                else return false
            }
            else -> {
                opts.logger().error("Command ${opts.command} not defined...")
                return false
            }
        }

        return true
    }

    private fun parseConfigIfExists(): Pair<GlowCommandBuildOptions?, String> {
        val configFile = File("glow.json")

        if (!configFile.exists())
            return null to ""

        val configJson = JSONObject(configFile.readText())
        val configVersion = configJson.string("v")

        return GlowCommandBuildOptions(
                inputDir = File(configJson.string("input", "posts")),
                outputDir = File(configJson.string("output", "out")),
                themeDir = File(configJson.string("theme", "themes/simple")),
                clearOutputDir = configJson.boolean("clearOutput", false),
                blogTitle = configJson.string("title", "<: Unknown Blog :>")) to configVersion
    }
}