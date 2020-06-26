interface NotesDataRenderer {
    fun renderRss(blog: BlogVM, notes: List<Note>): RssFeed
}

override fun renderRss(blog: BlogVM, notes: List<Note>): RssFeed {
    return RssFeed(
        filePath = "/rss/notes.xml",
        content = buildRss(blog, notes).generate()
    )
}

NotesPlugin {
    // FIXME implement as a separate plugin
    private fun buildRss(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildRss) return

        val rss = dataRenderer.renderRss(blog, notes)

        dataPublisher.publish(rss)
    }
}
