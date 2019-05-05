package io.github.dector.legacy.glow.cli

import com.beust.jcommander.JCommander
import io.github.dector.glow.Constants.CurrentConfigVersion
import io.github.dector.glow.core.logger.UILogger
import io.github.dector.glow.core.logger.logger
import io.github.dector.legacy.glow.builder.GlowBuilder
import io.github.dector.legacy.glow.creator.GlowProjectCreator
import io.github.dector.legacy.glow.utils.boolean
import io.github.dector.legacy.glow.utils.string
import org.json.JSONObject
import java.io.File

@Deprecated("Use new version instead")
class GlowCli(
        private val optionsProcessor: IOptionsProcessor = defaultOptionsProcessor()) {

    fun execute(vararg args: String) {
//        val stopWatch = StopWatch().start()

        val opts = parseArguments(args = *args)
//        if (opts.commandMainOptions.quiet)
//            disableUiLogger()

//        UILogger.info(CLI_HEADER)

//        if (!optionsProcessor.process(opts)) {
//            UILogger.info("\nFailed after ${stopWatch.stop().timeFormatted()}.")
//            exitProcess(1)
//        }
//
//        UILogger.info("\nFinished in ${stopWatch.stop().timeFormatted()}.")
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
                    UILogger.info(opts.commandMainOptions.usageText)
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
            UILogger.info("[Preparation] Config file found. CLI arguments will be ignored...")
            configBuildOpts
        } else {
            UILogger.info("[Preparation] Config file not found. CLI arguments will be used...")
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