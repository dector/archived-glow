package io.github.dector.glow.core.vm

import io.github.dector.glow.config.LegacyProjectConfig
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.config.NavigationItem

fun buildBlogVM(config: LegacyProjectConfig) = BlogVM(
    title = config.blog.title,
    navigation = config.blog.navigation.map {
        NavigationItem(path = it.path, title = it.title, type = it.type)
    },
    footer = FooterVM(
        author = config.blog.footer.author,
        year = config.blog.footer.year,
        licenseName = config.blog.footer.licenseName,
        licenseUrl = config.blog.footer.licenseUrl
    )
)
