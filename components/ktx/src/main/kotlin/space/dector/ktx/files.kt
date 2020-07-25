package space.dector.ktx

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.WatchEvent
import java.nio.file.attribute.BasicFileAttributes


operator fun File.div(path: String) = resolve(path)

fun File.ensureParentDirectoryExists() = apply { parentFile.mkdirs() }

class FileWatcher {

    private val watcher = FileSystems.getDefault().newWatchService()

    fun watchRecursively(file: File, vararg events: WatchEvent.Kind<*>, action: () -> Unit) {
        val path = file.toPath()

        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?): FileVisitResult {
                dir.register(watcher, events)

                return FileVisitResult.CONTINUE
            }
        })

        var process = true
        while (process) {
            val watcherKey = watcher.take()
            if (watcherKey == null) {
                process = false
                continue
            }

            val receivedEvents = watcherKey.pollEvents()
            if (receivedEvents.isNotEmpty())
                action()

            watcherKey.reset()
        }
    }
}
