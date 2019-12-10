package io.github.dector.glow.plugins.rss

import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import com.rometools.rome.io.SyndFeedOutput
import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.plugins.notes.Note2
import java.util.*

fun buildRss(blog: BlogVM, notes: List<Note2>): RssFile {
    val feed = SyndFeedImpl()

    feed.apply {
        title = blog.title

        feedType = "atom_1.0"
        language = "en"
        generator = "GlowRSSPlugin"
        publishedDate = Date()

        entries = notes.map(Note2::asFeedEntry)
    }

    /*val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <feed xmlns="http://www.w3.org/2005/Atom">
        
         <title>{{ site.title }}</title>
         <link href="{{ site.url }}/atom.xml" rel="self"/>
         <link href="{{ site.url }}/"/>
         <updated>{{ site.time | date_to_xmlschema }}</updated>
         <id>{{ site.url }}</id>
         <author>
           <name>{{ site.author.name }}</name>
           <email>{{ site.author.email }}</email>
         </author>
        
         {% for post in site.posts %}
         <entry>
           <title>{{ post.title }}</title>
           <link href="{{ site.url }}{{ post.url }}"/>
           <updated>{{ post.date | date_to_xmlschema }}</updated>
           <id>{{ site.url }}{{ post.id }}</id>
           <content type="html">{{ post.content | xml_escape }}</content>
         </entry>
         {% endfor %}
        
        </feed>
        """.trimIndent()*/

    return RssFile(
        filename = "atom.xml",
        content = feed.generate()
    )
}

data class RssFile(val filename: String, val content: String)

internal fun Note2.asFeedEntry() = let { note ->
    SyndEntryImpl().apply {
        title = note.title
    }
}

internal fun SyndFeed.generate() = SyndFeedOutput().outputString(this)

