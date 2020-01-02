package io.github.dector.glow.templates.hyde

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.config.NavigationItem
import io.github.dector.glow.core.theming.Theme
import io.github.dector.glow.di.DI
import io.github.dector.glow.di.get
import io.github.dector.glow.plugins.notes.Note2VM
import io.github.dector.glow.templates.hyde.layouts.noteContent
import io.github.dector.glow.templates.hyde.layouts.notesIndexContent
import io.github.dector.glow.templates.hyde.layouts.webPage
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import java.nio.file.Path

object Hyde {

    object Includes {

        fun head(head: HEAD, title: String) = head.apply {
            link(href = "http://gmpg.org/xfn/11", rel = "profile")
            meta {
                attributes["http-equiv"] = "X-UA-Compatible"
                content = "IE=edge"
            }
            meta {
                attributes["http-equiv"] = "content-type"
                content = "text/html; charset=utf-8"
            }

            // Enable responsiveness on mobile devices
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0, maximum-scale=1"
            }

            // CSS
            link {
                rel = "stylesheet"
                href = assetPath("css/poole.css")
            }
            link {
                rel = "stylesheet"
                href = assetPath("css/syntax.css")
            }
            link {
                rel = "stylesheet"
                href = assetPath("css/hyde.css")
            }
            link {
                rel = "stylesheet"
                href = assetPath("css/fonts.css")
            }

            // Icons
            link {
                rel = "shortcut icon"
                href = assetPath("favicon.ico")
            }

            /*+"<!-- RSS -->"
            link {
                rel = "alternate"
                type = "application/rss+xml"
                title = "RSS"
                href = "/atom.xml"
            }*/
        }

        fun sidebar(body: BODY, blog: BlogVM, currentNavItem: NavigationItem?) = body.apply {
            div("sidebar") {
                div("container sidebar-sticky") {
                    div("sidebar-about") {
                        h1 {
                            a(href = "/") {
                                +blog.title
                            }
                        }

                        if (blog.description.isNotBlank()) {
                            p("lead") { +blog.description }
                        }
                    }

                    nav("sidebar-nav") {
                        /*a("sidebar-nav-item{% if page.url == site.baseurl %} active{% endif %}") {
                            href = "{{ site.baseurl }}"
                            +"""Home"""
                        }*/

                        /*
                        {% comment %}
                          The code below dynamically generates a sidebar nav of pages with
                          `layout: page` in the front-matter. See readme for usage.
                        {% endcomment %}
                        */

                        blog.navigation.forEach { item ->
                            // Include only pages
//                            a("sidebar-nav-item{% if page.url == node.url %} active{% endif %}") {
                            a(href = item.path) {
                                classes = setOf("sidebar-nav-item").let {
                                    if (item == currentNavItem) {
                                        it + "active"
                                    } else it
                                }

                                +item.title
                            }
                        }

                        /*a("sidebar-nav-item") {
                            href = "{{ site.github.repo }}/archive/v{{ site.version }}.zip"
                            +"""Download"""
                        }*/
                        /*a("sidebar-nav-item") {
                            href = "{{ site.github.repo }}"
                            +"""GitHub project"""
                        }*/
                        /*span("sidebar-nav-item") { +"""Currently v{{ site.version }}""" }*/
                    }

                    //p { +"""&copy; {{ site.time | date: '%Y' }}. All rights reserved.""" }
                }
            }
        }
    }
}

class HydeTheme : Theme {

    override fun notesIndex(blog: BlogVM, notes: List<Note2VM>) =
        webPage(blog, blog.notesNavigationItem()) {
            notesIndexContent(notes)
        }

    override fun note(blog: BlogVM, note: Note2VM) =
        webPage(blog, blog.notesNavigationItem()) {
            noteContent(blog, note)
        }
}

private fun BlogVM.notesNavigationItem() = navigation.find { it.type == NavItemType.Notes }

internal fun assetPath(path: String, dirPath: Path): String =
    dirPath.resolve(path).toString()

private fun assetPath(path: String, config: Config = DI.get()): String =
    assetPath(path, config.glow.assets.targetPath)
