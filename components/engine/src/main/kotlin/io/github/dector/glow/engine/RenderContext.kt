package io.github.dector.glow.engine

data class RenderContext(
    val blog: BlogVM,
    val currentNavSection: NavItemVM,
    val paging: Paging
)

data class Paging(
    val current: Int,
    val total: Int,
    val prevPageUrl: WebPagePath? = null,
    val nextPageUrl: WebPagePath? = null
)
