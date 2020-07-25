package space.dector.glow.plugins.rss

import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import com.rometools.rome.io.SyndFeedOutput
import space.dector.glow.core.BlogVM
import space.dector.glow.plugins.notes.Note
import java.util.Date

fun buildRss(blog: BlogVM, notes: List<Note>): SyndFeed {
    val feed = SyndFeedImpl()

    feed.apply {
        title = blog.title

        feedType = "atom_1.0"
        language = "en"
        generator = "GlowRSSPlugin"
        publishedDate = Date()

        entries = notes.map(Note::asFeedEntry)
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

    return feed
}

internal fun Note.asFeedEntry() = let { note ->
    SyndEntryImpl().apply {
        title = note.title
    }
}

internal fun SyndFeed.generate() = SyndFeedOutput().outputString(this)

