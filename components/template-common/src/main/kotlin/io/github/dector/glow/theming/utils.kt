package io.github.dector.glow.theming

import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.NavItemType


fun BlogVM.notesNavigationItem() = navigation.find { it.type == NavItemType.Notes }
