package io.github.dector.glow.templates.hyde

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Paths

class HydeKtTest : StringSpec({

    "assetsPath" {
        val assetsPath = Paths.get("/assets")

        assetPath("css/file.css", assetsPath) shouldBe "/assets/css/file.css"
        assetPath("js/file.js", assetsPath) shouldBe "/assets/js/file.js"
    }
})
