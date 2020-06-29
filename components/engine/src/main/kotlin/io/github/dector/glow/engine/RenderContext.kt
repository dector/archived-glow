package io.github.dector.glow.engine

import io.github.dector.glow.coordinates.Coordinates

data class RenderContext(
    val blog: BlogVM,
    val currentNavSection: NavItemVM,
    val paging: Paging
)

data class Paging(
    val current: Int,
    val total: Int,
    val prevPage: Coordinates.Endpoint? = null,
    val nextPage: Coordinates.Endpoint? = null
)

fun Paging.hasPaging(): Boolean =
    prevPage != null || nextPage != null
