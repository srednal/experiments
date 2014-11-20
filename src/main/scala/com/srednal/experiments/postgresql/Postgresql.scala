package com.srednal.snug.experiments.postgresql

import com.srednal.snug.experiments.{Moons, Planets}
import org.postgresql.util.PGobject
import java.sql._
import scala.annotation.tailrec
import scala.util.Try
import net.liftweb.json._

object Postgresql extends App {

  implicit val formats = net.liftweb.json.DefaultFormats

  def jsonSqlObj[A](a: A) = {
    val dataObject = new PGobject()
    dataObject.setType("json")
    dataObject.setValue(compactRender(Extraction.decompose(a)))
    dataObject
  }

  @tailrec
  def printRs(rs: ResultSet): Unit = if (rs.next()) {
    println(rs.getObject("data"))
    printRs(rs)
  }


  Class.forName("org.postgresql.Driver")


  val db = DriverManager.getConnection("jdbc:postgresql:test", "dlanders", null)

  val stmt = db.createStatement()

  Try(stmt.execute("DROP TABLE Planets"))
  Try(stmt.execute("DROP TABLE Moons"))

  stmt.execute("CREATE TABLE Planets ( number integer, data json )")

  val insertPlanets = db.prepareStatement("INSERT INTO Planets VALUES ( ?,  ? )")

  Planets.planets foreach { p =>
    insertPlanets.clearParameters()
    insertPlanets.setInt(1, p.number)
    insertPlanets.setObject(2, jsonSqlObj(p))
    insertPlanets.execute()
  }

  stmt.execute("CREATE TABLE Moons ( planet varchar, data json )")

  val insertMoons = db.prepareStatement("INSERT INTO Moons VALUES ( ?, ? )")

  Moons.moons foreach { m =>
    insertMoons.clearParameters()
    insertMoons.setString(1, m.planet)
    insertMoons.setObject(2, jsonSqlObj(m))
    insertMoons.execute()
  }

  println("rings:")
  printRs(
    stmt.executeQuery("SELECT data FROM Planets WHERE CAST(data->>'rings' AS Boolean) = true")
  )

  println("marz:")
  printRs {
    val ps = db.prepareStatement("SELECT data FROM Planets WHERE data->>? = ?")
    ps.setString(1, "name")
    ps.setString(2, "Mars")
    ps.executeQuery()
  }

  println("earthish:")
  printRs(stmt.executeQuery("SELECT data FROM Planets WHERE CAST(data->>'gravity' AS Float) between 9 and 11"))

  println("heavy:")
  printRs(stmt.executeQuery("SELECT data FROM Planets WHERE CAST(data->>'gravity' AS Float) > 15"))

  println("bouncy:")
  printRs(stmt.executeQuery("SELECT data FROM Planets WHERE CAST(data->>'gravity' AS Float) < 8"))

  println("I*:")
  printRs(stmt.executeQuery("SELECT data FROM Moons WHERE lower(data->>'name') LIKE lower('i%')"))

}
