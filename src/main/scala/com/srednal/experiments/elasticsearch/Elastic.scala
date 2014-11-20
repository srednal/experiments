package com.srednal.snug.experiments.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.srednal.snug.experiments.{Moons, Planets}
import org.elasticsearch.common.settings.ImmutableSettings
import scala.collection.JavaConverters._


object Elastic extends App {

  // insert some data
  //  val client = ElasticClient.local

  val settings = ImmutableSettings.settingsBuilder().put("http.enabled", false).put("path.home", "build/elastic/")
  val client = ElasticClient.local(settings.build)

  //
  //  // await is a helper method to make this operation sync instead of async
  //  val ixResp = client.execute {index into "bands/artists" fields "name" -> "coldplay"}.await
  //
  //  // refresh indices so things are searchable
  //  client.client.admin().indices().prepareRefresh("_all").execute().actionGet()


  //  val resp = client.execute {search in "bands" -> "artists" query "coldplay"}.await
  //  println(resp)

  client.execute {
    bulk({
      Planets.planets.map { p =>
        index into "sol" -> "planets" fields(
          "name" -> p.name,
          "number" -> p.number,
          "mass" -> p.mass,
          "diameter" -> p.diameter,
          "gravity" -> p.gravity,
          "dayLength" -> p.dayLength,
          "distanceFromSun" -> p.distanceFromSun,
          "orbitPeriod" -> p.orbitPeriod,
          "tilt" -> p.tilt,
          "meanTemp" -> p.meanTemp,
          "rings" -> p.rings
          )
      } ++
        Moons.moons.map { m =>
          index into "sol" -> "moons" fields(
            "planet" -> m.planet,
            "name" -> m.name
            )
        }
    }: _*
    )
  }.await

  // refresh indices so things are searchable now
  client.client.admin().indices().prepareRefresh("sol").execute().actionGet()


  println(client.execute {search in "sol" -> "planets" query {term("rings", true)}}.await)

  // earth-ish gravity
  println(client.execute {search in "sol" -> "planets" query {rangeQuery("gravity") from 9 to 11}}.await)

  // heavy
  println(client.execute {search in "sol" -> "planets" query {rangeQuery("gravity") from 15}}.await)

  // bouncy
  println(client.execute {search in "sol" -> "planets" query {rangeQuery("gravity") to 8}}.await)

  // ??? This matches Io:
  println(client.execute {search in "sol" -> "moons" query {term("name", "io")}}.await)
  // but this does not: ????
  println(client.execute {search in "sol" -> "moons" query {term("name", "Io")}}.await)

  //  println(client.execute {
  //    search in "sol" -> "planets" query {
  //      rangeQuery("number") from 1 to 7
  //    } sort (
  //      by field "name"
  //      ) start 2 limit 3
  //
  //  }.await)

}


