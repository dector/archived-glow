package space.dector.glow.vm

import space.dector.glow.config.WebsiteConfig
import space.dector.glow.engine.BlogVM
import space.dector.glow.engine.FooterVM
import space.dector.glow.engine.NavItemVM

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
    ),
    githubUser = config.githubUser
)
