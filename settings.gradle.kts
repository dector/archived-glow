rootProject.name = "glow"

include(
    "glow-common",
    "glow-server",
    "glow-cli",
    "templates-hyde"
)

// Include projects from `components/` dir
File("components")
    .listFiles()!!
    .asSequence()
    .filter { it.isDirectory }
    .filter { File(it, "build.gradle.kts").exists() }
    .forEach { dir ->
        val projectName = ":${dir.name}"
        include(projectName)
        project(projectName).projectDir = File("${dir.parent}/${dir.name}")
    }
