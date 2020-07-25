package space.dector.glow.utils

typealias TimeFormatter = (Long) -> String

class StopWatch {

    companion object {

        val DefaultSecondsFormatter: TimeFormatter = { time ->
            StringBuilder().apply {
                val dTime = decomposeTimeMs(time)

                val fractionSeconds = time.msFractionPartInMs()

                append(dTime.seconds)
                if (time.msLessThanMinutes(1) && dTime.millisFraction > 0) {
                    append(".").append(fractionSeconds)
                }
                append("s")

                if (dTime.minutes > 0) {
                    insert(0, "${dTime.minutes}m ")
                }

                if (dTime.hours > 0) {
                    insert(0, "${dTime.hours}h ")
                }

                if (dTime.days > 0) {
                    insert(0, "${dTime.days}d ")
                }
            }.toString()
        }
    }

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

    fun timeFormatted(formatter: TimeFormatter = DefaultSecondsFormatter): String = formatter(time)
}
