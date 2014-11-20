package com.srednal.experiments.caching

import akka.util.Timeout
import scala.concurrent.Future
import akka.actor.ActorSystem
import spray.caching.{LruCache, Cache}
import spray.util._
import scala.concurrent.duration._

object SprayCache extends App {

  val system = ActorSystem()
  import system.dispatcher

  // if we have an "expensive" operation
  def expensiveOp(): Double = new util.Random().nextDouble()

  // and a Cache for its result type
  // Create a new ExpiringLruCache or SimpleLruCache based on non-zero/finite timeToLive and/or timeToIdle.
  // TTL is the maximum time an entry remains in the cache (put -> expire)
  // TTI is the maximum time an entry is kept without having been accessed (last get -> expire)
  // TTL must be > TTI
  val cache: Cache[Double] = LruCache(
    maxCapacity = 20,
    timeToLive = 2.second,
    timeToIdle = 1.seconds)

  // we can wrap the operation with caching support
  // (providing a caching key)
  def cachedOp[T](key: T): Future[Double] = cache(key) {expensiveOp()}

  // Play with LRU, TTL and TTI

  'a' to 'z' map (_.toString) foreach cachedOp

  println(cache.size) // 20
  println(cache.ascendingKeys().mkString("")) // oldest first - ghijklmnopqrstuvwxyz

  Thread.sleep(600)

  'o' to 'u' map (_.toString) foreach cache.get // reset TTI for o to u

  Thread.sleep(600) // past TTI for all except o to u

  println("---2")

  cachedOp("A") // evicts g as it is the oldest TTI

  println(cache.size) // 20
  // force eviction - all but o to u, AA should be evicted as they are past their TTI
  println(cache.ascendingKeys().map { k => cache.get(k); k}.mkString("")) // hijklmnvwxyzopqrstuA
  println(cache.ascendingKeys().mkString("")) // opqrstuA
  println(cache.size) // 8


  Thread.sleep(900) // past TTL for all except A

  println("---3")

  // force eviction - all but A
  println(cache.ascendingKeys().map { k => cache.get(k); k}.mkString("")) // opqrstuA
  println(cache.ascendingKeys().mkString("")) // A
  println(cache.size) // 1

  // Play with futureisms

  println("----------")

  cache.clear()

  implicit val awaitTimeout: Timeout = 100.millis

  println(
    cachedOp("foo").await,
    cachedOp("foo").await,
    cache("foo") {0.0}.await
  )




  system.shutdown()

}
