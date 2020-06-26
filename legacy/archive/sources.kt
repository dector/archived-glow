Plugin {
    private fun buildArchive(blog: BlogVM, notes: List<Note>) {
        if (!runOptions.buildArchive) return

        "Notes archive".log()
        "Processing...".log()
        val webPage = dataRenderer.renderNotesArchive(blog, notes)

        "Publishing...".log()
        dataPublisher.publish(webPage)
    }
}
