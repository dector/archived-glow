@file:Suppress("NOTHING_TO_INLINE")

package io.github.dector.glow.utils

inline fun Long.msToSec(): Long = this / 1000
inline fun Long.msToMin(): Long = this.msToSec().secToMin()
inline fun Long.msToHour(): Long = this.msToMin().minToHour()
inline fun Long.msToDay(): Long = this.msToHour().hourToDay()

inline fun Long.secToMin(): Long = this / 60

inline fun Long.minToHour(): Long = this / 60

inline fun Long.hourToDay(): Long = this / 24

inline fun Long.msFractionPartInMs(): Long = (this % 1000) / 100
inline fun Long.secPartInMs(): Long = this.msToSec() % 1000
inline fun Long.minPartInMs(): Long = this.msToMin() % 60
inline fun Long.hourPartInMs(): Long = this.msToHour() % 60
inline fun Long.daysPartInMs(): Long = this.msToDay() % 24

fun decomposeTimeMs(time: Long): Time = Time(
        millisFraction = time.msFractionPartInMs(),
        seconds = time.secPartInMs(),
        minutes = time.minPartInMs(),
        hours = time.hourPartInMs(),
        days = time.daysPartInMs())

data class Time(val millisFraction: Long, val seconds: Long, val minutes: Long, val hours: Long, val days: Long)

inline fun Long.msLessThanMinutes(min: Int): Boolean = this.msToMin() <= min