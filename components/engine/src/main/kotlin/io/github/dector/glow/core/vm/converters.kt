package io.github.dector.glow.core.vm

import io.github.dector.glow.config.WebsiteConfig
import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.FooterVM
import io.github.dector.glow.engine.NavItemVM

fun buildBlogVM(config: WebsiteConfig) = BlogVM(
    title = config.title,
    navigation = config.navigation.map {
        NavItemVM(title = it.title, path = it.path)
    },
    footer = FooterVM(
        author = config.footerAuthor,
        year = config.footerYear,
        licenseName = config.footerLicenseName,
        licenseUrl = config.footerLicenseUrl
    )
)
