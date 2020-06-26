rootProject.name = "glow"

include(
    "glow-common",
    "glow-server",
    "glow-cli",
    "templates-hyde"
)

fun findGradleProjectsIn(dir: String): Sequence<File> = File(dir)
    .listFiles()!!
    .asSequence()
    .filter { it.isDirectory }
    .filter { File(it, "build.gradle.kts").exists() }

// Include projects from `components/` dir
findGradleProjectsIn("components").forEach { dir ->
    val projectName = ":${dir.name}"
    include(projectName)
    project(projectName).projectDir = File("${dir.parent}/${dir.name}")
}

// Include projects from `apps/` dir with `app-` prefix
findGradleProjectsIn("apps").forEach { dir ->
    val projectName = ":app-${dir.name}"
    include(projectName)
    project(projectName).projectDir = File("${dir.parent}/${dir.name}")
}
