package io.github.dector.glow.templates.hyde

import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.unsafe

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

            unsafe { +"<!-- Enable responsiveness on mobile devices-->" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1.0, maximum-scale=1"
            }

            title {
                +title
            }

            unsafe { +"""<!-- CSS -->""" }
            link {
                rel = "stylesheet"
                href = "public/css/poole.css"
            }
            link {
                rel = "stylesheet"
                href = "public/css/syntax.css"
            }
            link {
                rel = "stylesheet"
                href = "public/css/hyde.css"
            }
            link {
                rel = "stylesheet"
                href = "http://fonts.googleapis.com/css?family=PT+Sans:400,400italic,700|Abril+Fatface"
            }

            unsafe { +"<!-- Icons -->" }
            link {
                rel = "apple-touch-icon-precomposed"
                sizes = "144x144"
                href = "public/apple-touch-icon-144-precomposed.png"
            }
            link {
                rel = "shortcut icon"
                href = "public/favicon.ico"
            }

            /*+"<!-- RSS -->"
            link {
                rel = "alternate"
                type = "application/rss+xml"
                title = "RSS"
                href = "/atom.xml"
            }*/
        }

        fun sidebar(body: BODY) = body.apply {
            div("sidebar") {
                div("container sidebar-sticky") {
                    div("sidebar-about") {
                        h1 {
                            a {
                                href = "{{ site.baseurl }}"
                                +"""{{ site.title }}"""
                            }
                        }
                        p("lead") { +"""{{ site.description }}""" }
                    }

                    nav("sidebar-nav") {
                        a("sidebar-nav-item{% if page.url == site.baseurl %} active{% endif %}") {
                            href = "{{ site.baseurl }}"
                            +"""Home"""
                        }

                        +"""{% comment %}
        The code below dynamically generates a sidebar nav of pages with
        `layout: page` in the front-matter. See readme for usage.
      {% endcomment %}

      {% assign pages_list = site.pages %}
      {% for node in pages_list %}
        {% if node.title != null %}
          {% if node.layout == "page" %}"""
                        a("sidebar-nav-item{% if page.url == node.url %} active{% endif %}") {
                            href = "{{ node.url }}"
                            +"""{{ node.title }}"""
                        }
                        +"""{% endif %}
        {% endif %}
      {% endfor %}"""

                        a("sidebar-nav-item") {
                            href = "{{ site.github.repo }}/archive/v{{ site.version }}.zip"
                            +"""Download"""
                        }
                        a("sidebar-nav-item") {
                            href = "{{ site.github.repo }}"
                            +"""GitHub project"""
                        }
                        span("sidebar-nav-item") { +"""Currently v{{ site.version }}""" }
                    }

                    p { +"""&copy; {{ site.time | date: '%Y' }}. All rights reserved.""" }
                }
            }
        }
    }
}
