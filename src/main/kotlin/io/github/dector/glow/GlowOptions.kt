package io.github.dector.glow

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import org.slf4j.LoggerFactory
import java.io.File

class GlowOptions {

    @Parameter(names = arrayOf("-i", "--input"), required = true, converter = FileConverter::class)
    var inputDir: File? = null

    @Parameter(names = arrayOf("-o", "--output"), required = true, converter = FileConverter::class)
    var outputDir: File? = null

    @Parameter(names = arrayOf("-t", "--theme"), required = true, converter = FileConverter::class)
    var themeDir: File? = null
}

class OptionsValidator(val opts: GlowOptions) {
    
    private val logger = LoggerFactory.getLogger(javaClass)
    
    fun validate(): Boolean
            = validateInputDir(opts.inputDir)
            && validateOutputDir(opts.outputDir)
            && validateThemeDir(opts.themeDir)

    fun validateInputDir(dir: File?): Boolean {
        assert("Input dir should be set", logger) { dir != null } ?: return false
        assert("Input dir should be directory", logger) { dir?.isDirectory } ?: return false

        File(dir, "posts/").also {
            assert("Input dir should have posts directory", logger) { it.exists() && it.isDirectory }
        }

        return true
    }

    fun validateOutputDir(dir: File?): Boolean {
        assert("Output dir should be set", logger) { dir != null} ?: return false
        assert("Output dir should not exist", logger) { dir?.exists().isNullOrFalse() || dir?.listFiles()?.isEmpty().isTrue() }
                ?: dir?.mkdirs()
        
        return true
    }

    fun validateThemeDir(dir: File?): Boolean {
        assert("Theme dir should be set", logger) { dir != null } ?: return false
        assert("Theme fir should contain `page.twig`", logger) { File(dir, "page.twig").exists() }

        return true
    }
}