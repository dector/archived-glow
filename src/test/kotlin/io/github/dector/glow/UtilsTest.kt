package io.github.dector.glow

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.core.NavigationItem
import io.github.dector.glow.core.WebPagePath
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class UtilsTest : BehaviorSpec({

    Given("blog view model") {
        val blog = BlogVM(
            navigation = listOf(
                NavigationItem("/", "Home", NavItemType.Home),
                NavigationItem("/notes", "Notes", NavItemType.Notes),
                NavigationItem("/projects", "Projects", NavItemType.Projects),
                NavigationItem("/about", "About", NavItemType.About)
            )
        )

        val cases = mapOf(
            WebPagePath("/") to "/",
            WebPagePath("index.html") to "/",
            WebPagePath("/index.html") to "/",
            WebPagePath("/notes/index.html") to "/notes",
            WebPagePath("/projects/some/index.html") to "/projects",
            WebPagePath("/about/some.html") to "/about"
        )

        cases.forEach { (pagePath, expectedNavItemPath) ->
            When("page path is '${pagePath.value}'") {
                val navItemPath = blog.detectNavItem(pagePath)?.path

                Then("nav item path is $expectedNavItemPath") {
                    navItemPath shouldBe expectedNavItemPath
                }
            }
        }
    }

    Given("") {
        val cases = mapOf(
            WebPagePath("/") to "/",
            WebPagePath("index.html") to "/",
            WebPagePath("/index.html") to "/",
            WebPagePath("/notes/index.html") to "/notes",
            WebPagePath("/projects/some/index.html") to "/projects/some",
            WebPagePath("/about/some.html") to "/about"
        )

        cases.forEach { (pagePath, expectedFolderPath) ->
            When("page path is '${pagePath.value}'") {
                val folderPath = pagePath.pathToFolder()

                Then("folder path is $expectedFolderPath") {
                    folderPath shouldBe expectedFolderPath
                }
            }
        }
    }
})
