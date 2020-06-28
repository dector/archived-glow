package io.github.dector.glow.coordinates


sealed class Coordinates {

    // Pointing to exact file
    class File(val parent: Endpoint, val name: String) : Coordinates()

    // Ex: (base: 'localhost', section: 'notes', inner: 'archive', name: 'page2') -> 'localhost/notes/archive/page2/'
    //     (base: 'localhost', section: null, inner: null, name: 'cv') -> 'localhost/cv/'
    data class Endpoint(
//        val base: String,
        val section: String = "",
        val inner: String = "",
        val name: String = ""
    ) : Coordinates()

    companion object {
        val Empty = Endpoint()
    }
}

// TODO add tests for edge cases
fun Coordinates.Endpoint.inHostPath(useLeadingSlash: Boolean = true, useTrailingSlash: Boolean = true): String {
    var path = buildString {
        section.takeIf(String::isNotEmpty)?.let {
            append(it)
            append('/')
        }
        inner.takeIf(String::isNotEmpty)?.let {
            append(it)
            append('/')
        }
        name.takeIf(String::isNotEmpty)?.let {
            append(it)
        }
    }.trim('/')

    if (path.isEmpty()) return ""

    if (useLeadingSlash) {
        path = "/$path"
    }
    if (useTrailingSlash) {
        path = "$path/"
    }

    return path
}

fun Coordinates.Endpoint.withFile(name: String) =
    Coordinates.File(
        parent = this,
        name = name)

fun Coordinates.File.inHostPath(useLeadingSlash: Boolean = true): String =
    parent.inHostPath(useLeadingSlash = useLeadingSlash, useTrailingSlash = true) + name
