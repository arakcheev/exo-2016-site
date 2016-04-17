/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

package models

trait Callbacks[T] {

  val callbacks = scala.collection.mutable.MutableList.empty[(T => Unit)]

  def &(item: T): Unit = {
    callbacks.foreach(_ (item))
  }

  def callback(f: (T) => Unit): Unit = {
    callbacks += f
  }
}
