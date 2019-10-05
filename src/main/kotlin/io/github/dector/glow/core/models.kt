package io.github.dector.glow.core

data class BlogVM(
    val title: String = "",
    val footer: FooterVM = FooterVM(),
    val navigation: List<NavigationItem> = emptyList()
)

data class FooterVM(
    val author: String = "",
    val year: String = "",
    val licenseName: String = "",
    val licenseUrl: String = ""
)
