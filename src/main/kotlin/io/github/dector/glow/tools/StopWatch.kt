package io.github.dector.glow.tools

class StopWatch {

    var startTime: Long = 0
        private set

    var stopTime: Long = 0
        private set

    val time: Long
        get() = if (stopped) stopTime - startTime else 0

    private var stopped = false

    fun start(): StopWatch {
        if (stopped)
            throw IllegalStateException("Stopwatch is already stopped")

        startTime = System.currentTimeMillis()

        return this
    }

    fun stop(): StopWatch {
        stopped = true

        stopTime = System.currentTimeMillis()

        return this
    }

    fun timeFormatted(): String = StringBuilder().apply {
        val timeSec = time / 1000
        append(timeSec)

        val timePartSec = (time % 1000) / 100
        if (timePartSec > 0) {
            append(".").append(timePartSec)
        }
        append("s")

        val timeMin = timeSec / 60
        if (timeMin > 0) {
            insert(0, timeMin).append("m ")
        }

        val timeHours = timeMin / 60
        if (timeHours > 0) {
            insert(0, timeHours).append("h ")
        }

        val timeDays = timeHours / 24
        if (timeDays > 0) {
            insert(0, timeDays).append("d ")
        }
    }.toString()
}