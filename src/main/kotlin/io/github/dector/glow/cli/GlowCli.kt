package io.github.dector.glow.cli

import com.beust.jcommander.JCommander
import io.github.dector.glow.AppVersion
import io.github.dector.glow.CliHeader
import io.github.dector.glow.CurrentConfigVersion
import io.github.dector.glow.builder.GlowBuilder
import io.github.dector.glow.creator.GlowProjectCreator
import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.logger.disableUiLogger
import io.github.dector.glow.logger.logger
import io.github.dector.glow.tools.StopWatch
import io.github.dector.glow.tools.boolean
import io.github.dector.glow.tools.string
import org.json.JSONObject
import java.io.File
import kotlin.system.exitProcess

class GlowCli(
        private val optionsProcessor: IOptionsProcessor = defaultOptionsProcessor()) {

    fun execute(vararg args: String) {
        val stopWatch = StopWatch().start()

        val opts = parseArguments(args = *args)
        if (opts.commandMainOptions.quiet)
            disableUiLogger()

        UiLogger.info(CliHeader)

        if (!optionsProcessor.process(opts)) {
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

        if (commandMain.help)
            commandMain.usageText = StringBuilder().also { jc.usage(it) }.toString()

        return baseOpts.copy(
                command = jc.parsedCommand ?: "",
                commandMainOptions = commandMain,
                commandInitOptions = commandInit,
                commandBuildOptions = commandBuild)
    }
}

interface IOptionsProcessor {

    fun process(opts: GlowOptions): Boolean
}

private class DefaultOptionsProcessor : IOptionsProcessor {

    val logger = logger()

    override fun process(opts: GlowOptions) = when (opts.command) {
        GlowCommandInitOptions.Value -> processInitCommand(opts.commandInitOptions)
        GlowCommandBuildOptions.Value -> processBuildCommand(opts.commandBuildOptions)
        else -> {
            when {
                opts.commandMainOptions.help -> {
                    UiLogger.info(opts.commandMainOptions.usageText)
                    true
                }
                opts.command.isEmpty() -> true
                else -> {
                    opts.logger().error("Command `${opts.command}` not defined...")
                    false
                }
            }
        }
    }

    private fun processInitCommand(opts: GlowCommandInitOptions): Boolean {
        return OptionsValidator().validateInitCommand(opts)
                && GlowProjectCreator(opts).process()
    }

    private fun processBuildCommand(opts: GlowCommandBuildOptions): Boolean {
        val (configBuildOpts, configVersion) = parseConfigIfExists()

        if (configVersion != CurrentConfigVersion) {
            logger.error("Config version `$configVersion` is not supported. Actual version is `$CurrentConfigVersion`")
            return false
        }

        val buildOpts = if (configBuildOpts != null) {
            UiLogger.info("[Preparation] Config file found. CLI arguments will be ignored...")
            configBuildOpts
        } else {
            UiLogger.info("[Preparation] Config file not found. CLI arguments will be used...")
            opts
        }

        return OptionsValidator().validateBuildCommand(buildOpts)
                && GlowBuilder(buildOpts).process()
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

private fun defaultOptionsProcessor() = DefaultOptionsProcessor()