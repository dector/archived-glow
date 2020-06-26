package io.github.dector.glow.core.vm

import io.github.dector.glow.config.WebsiteConfig
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.config.NavigationItem

fun buildBlogVM(config: WebsiteConfig) = BlogVM(
    title = config.title,
    navigation = config.navigation.map {
        NavigationItem(path = it.path, title = it.title, type = NavItemType.from(it.sectionCode))
    },
    footer = FooterVM(
        author = config.footerAuthor,
        year = config.footerYear,
        licenseName = config.footerLicenseName,
        licenseUrl = config.footerLicenseUrl
    )
)
