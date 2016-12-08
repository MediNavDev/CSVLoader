package com.scienaptic.csvloader.utils


import java.util.concurrent.TimeUnit

class Stopwatch {
  var startTime = System.currentTimeMillis
  var elapsedTime: Long = 0

  def start() = {
    startTime = System.currentTimeMillis; elapsedTime = 0
  }

  def stop() = {
    elapsedTime = System.currentTimeMillis - startTime
  }

  override def toString: String = {
    val millis: Long = elapsedTime
    val unit: TimeUnit = chooseUnit(millis)
    val value: Double = millis.toDouble / TimeUnit.MILLISECONDS.convert(1, unit)
    f"$value%.4g ${abbreviate(unit)}"
  }

  private def chooseUnit(millis: Long): TimeUnit = {
    import TimeUnit._
    if (HOURS.convert(millis, MILLISECONDS) > 0) return HOURS
    if (MINUTES.convert(millis, MILLISECONDS) > 0) return MINUTES
    if (SECONDS.convert(millis, MILLISECONDS) > 0) return SECONDS
    MILLISECONDS
  }

  private def abbreviate(unit: TimeUnit): String = unit match {
    case TimeUnit.MILLISECONDS => "ms"
    case TimeUnit.SECONDS => "s"
    case TimeUnit.MINUTES => "min"
    case TimeUnit.HOURS => "h"
    case _ => throw new AssertionError
  }
}

object Stopwatch {
  def apply(): Stopwatch = new Stopwatch

  def createStarted(): Stopwatch = {
    val sw = new Stopwatch
    sw.start()
    sw
  }

  def time[T](block: => T): (T, Stopwatch) = {
    val sw = createStarted()
    val result = block
    sw.stop()
    (result, sw)
  }
}