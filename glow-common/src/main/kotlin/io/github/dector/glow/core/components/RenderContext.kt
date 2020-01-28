package io.github.dector.glow.core.components

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.config.NavigationItem

data class RenderContext(val blog: BlogVM, val navigationItem: NavigationItem)
