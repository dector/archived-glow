package io.github.dector.glow.v2.dumbimpl


fun html(title: String, body: () -> String): String {
    return """
        |<html>
        |<head><title>$title</title></head>
        |<body>
        |${body()}
        |</body>
        |</html>
        """.trimMargin()
}