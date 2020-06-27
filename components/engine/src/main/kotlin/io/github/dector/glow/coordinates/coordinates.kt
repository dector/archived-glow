package io.github.dector.glow.coordinates


sealed class Coordinates {

    // Pointing to exact file
    class File(val parent: Endpoint, val name: String) : Coordinates()

    // Ex: (base: 'localhost', section: 'notes', inner: 'archive', name: 'page2') -> 'localhost/notes/archive/page2/'
    //     (base: 'localhost', section: null, inner: null, name: 'cv') -> 'localhost/cv/'
    data class Endpoint(
//        val base: String,
        val section: String? = null,
        val inner: String? = null,
        val name: String
    ) : Coordinates()
}

fun Coordinates.Endpoint.inHostPath(useLeadingSlash: Boolean = true, useTrailingSlash: Boolean = true): String = buildString {
    if (useLeadingSlash)
        append('/')

    section?.let {
        append(it)
        append('/')
    }
    inner?.let {
        append(it)
        append('/')
    }

    append(name)

    if (useTrailingSlash)
        append('/')
}

fun Coordinates.Endpoint.withFile(name: String) =
    Coordinates.File(
        parent = this,
        name = name)

fun Coordinates.File.inHostPath(useLeadingSlash: Boolean = true): String =
    parent.inHostPath(useLeadingSlash = useLeadingSlash, useTrailingSlash = true) + name

