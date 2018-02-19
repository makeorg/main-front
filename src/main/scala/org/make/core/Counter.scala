package org.make.core

class Counter(initialValue: Int = 0) {

  private var internalValue: Int = initialValue

  def getAndIncrement(step: Int = 1): Int = {
    val result = internalValue
    internalValue += step
    result
  }

  def get: Int = internalValue

  def incrementAndGet(step: Int = 1): Int = {
    internalValue += step
    internalValue
  }

}

object Counter {
  val showcaseCounter: Counter = new Counter()
}
