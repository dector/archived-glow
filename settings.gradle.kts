rootProject.name = "glow"

fun includeAllFrom(collectionDir: String, prefix: String? = null) {
    fun findGradleProjectsIn(dir: String): Sequence<File> = File(dir)
        .listFiles()
        .let { it ?: emptyArray() }
        .asSequence()
        .filter { it.isDirectory }
        .filter { File(it, "build.gradle.kts").exists() }
        .filterNot { File(it, ".ignore-module").exists() }

    findGradleProjectsIn(collectionDir).forEach { dir ->
        val projectName = dir.name
            .let { if (prefix != null) "$prefix-$it" else it }
            .let { ":$it" }

        include(projectName)
        project(projectName).projectDir = File("${dir.parent}/${dir.name}")
    }
}

includeAllFrom("components", prefix = "component")
includeAllFrom("apps", prefix = "app")
