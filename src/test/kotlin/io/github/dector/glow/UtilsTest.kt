package io.github.dector.glow

import com.google.common.truth.Truth.assertThat
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.core.NavigationItem
import io.github.dector.glow.core.WebPagePath
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test
    fun detectNavItem() {
        val blog = BlogVM(
                navigation = listOf(
                        NavigationItem("/", "Home", NavItemType.Home),
                        NavigationItem("/notes", "Notes", NavItemType.Notes),
                        NavigationItem("/projects", "Projects", NavItemType.Projects),
                        NavigationItem("/about", "About", NavItemType.About)
                )
        )

        assertThat(blog.detectNavItem(WebPagePath("/"))?.path)
                .isEqualTo("/")
        assertThat(blog.detectNavItem(WebPagePath("index.html"))?.path)
                .isEqualTo("/")
        assertThat(blog.detectNavItem(WebPagePath("/index.html"))?.path)
                .isEqualTo("/")
        assertThat(blog.detectNavItem(WebPagePath("/notes/index.html"))?.path)
                .isEqualTo("/notes")
        assertThat(blog.detectNavItem(WebPagePath("/projects/some/index.html"))?.path)
                .isEqualTo("/projects")
        assertThat(blog.detectNavItem(WebPagePath("/about/some.html"))?.path)
                .isEqualTo("/about")
    }

    @Test
    fun pathToParent() {
        assertThat(WebPagePath("").pathToFolder())
                .isEqualTo("/")
        assertThat(WebPagePath("/").pathToFolder())
                .isEqualTo("/")
        assertThat(WebPagePath("/index.html").pathToFolder())
                .isEqualTo("/")
        assertThat(WebPagePath("index.html").pathToFolder())
                .isEqualTo("/")
        assertThat(WebPagePath("/notes/index.html").pathToFolder())
                .isEqualTo("/notes")
        assertThat(WebPagePath("/notes/some/").pathToFolder())
                .isEqualTo("/notes/some")
        assertThat(WebPagePath("/notes/some/index.html").pathToFolder())
                .isEqualTo("/notes/some")
    }
}