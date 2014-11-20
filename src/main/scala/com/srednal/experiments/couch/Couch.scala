package com.srednal.snug.experiments.couch

import akka.actor.ActorSystem
import akka.util._
import com.srednal.snug.experiments.{Moons, Planets}
import scala.concurrent.duration._

import gnieh.sohva._

case class CPlanet(_id: String,
                   name: String,
                   number: Int, // Mercury=0...
                   mass: Double, // 10^24 kg
                   diameter: Long, // km
                   gravity: Double, // m/s^2
                   dayLength: Double, //hours
                   distanceFromSun: Double, // 10^6km
                   orbitPeriod: Double, //days
                   tilt: Double, // deg
                   meanTemp: Int, // C
                   rings: Boolean) extends IdRev

case class CMoon(_id: String, name: String, planet: String) extends IdRev


object Couch extends App {

  //  import scala.concurrent.ExecutionContext.Implicits._

  val cplanets = Planets.planets map { p => CPlanet(p.name, p.name, p.number, p.mass, p.diameter, p.gravity, p.dayLength, p.distanceFromSun, p.orbitPeriod, p.tilt, p.meanTemp, p.rings)}
  val cmoons = Moons.moons map { m => CMoon(m.name, m.name, m.planet)}


  implicit val system = ActorSystem()
  implicit val timeout = Timeout(20.seconds)
  val couch = new sync.CouchClient


  val db = couch.database("sol")

  db.delete
  db.create

  cplanets foreach (db.saveDoc(_))
  cmoons foreach (db.saveDoc(_))

  val pdsn = db.design("planets")

  println("--- rings:")
  pdsn.saveView("rings", """function(doc){ if (doc.rings) emit(doc.rings, doc); }""")
  pdsn.view("rings").query[Boolean, CPlanet, CPlanet](key = Some(true)).values foreach println

  println("--- earthy gravity:")
  pdsn.saveView("gravity", """function(doc) { if (doc.gravity) emit(doc.gravity, doc); }""")
  pdsn.view("gravity").query[Double, CPlanet, CPlanet](startkey = Some(9), endkey = Some(11)).values foreach println

  println("--- heavy:")
  pdsn.view("gravity").query[Double, CPlanet, CPlanet](startkey = Some(15)).values foreach println

  println("--- bouncy:")
  pdsn.view("gravity").query[Double, CPlanet, CPlanet](endkey = Some(8)).values foreach println

  println("--- moons: Io*:")
  val mdsn = db.design("moons")
  mdsn.saveView("name", """function(doc) { if (doc.name) emit(doc.name, doc); }""")
  mdsn.view("name").query[String, CMoon, CMoon](startkey = Some("Io"), endkey = Some("Ip")).values foreach println

  couch.shutdown()
  system.shutdown()
}
