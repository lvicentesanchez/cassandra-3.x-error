import com.datastax.driver.core._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.google.common.cache.{ CacheBuilder, CacheLoader, LoadingCache }

import scala.collection.JavaConverters._

object CassandraOk extends App {

  val nrOfCacheEntries: Long = 100L

  val cluster: Cluster =
    Cluster.builder().
      addContactPoints("localhost").withPort(9042).
      withQueryOptions(new QueryOptions().setFetchSize(2)).
      build()

  val session: Session = cluster.newSession()

  val cache: LoadingCache[String, PreparedStatement] =
    CacheBuilder.newBuilder().
      maximumSize(nrOfCacheEntries).
      build(
        new CacheLoader[String, PreparedStatement]() {
          def load(key: String): PreparedStatement = session.prepare(key.toString)
        }
      )

  case class Measure(id: String, entry: Int, value: Int)

  object Measure {

    def getById(cache: LoadingCache[String, PreparedStatement], session: Session)(ids: String*): List[Measure] = {
      val query: Statement =
        QueryBuilder.select().
          all().
          from("db", "measure_ok").
          where(QueryBuilder.in("id", ids.map(_ => QueryBuilder.bindMarker()): _*))

      session.execute(cache.get(query.toString).bind(ids: _*)).
        iterator().asScala.
        map {
          row => Measure(row.getString("id"), row.getInt("entry"), row.getInt("value"))
        }.
        map { a => println(a); a }.
        to[List]
    }
  }

  Measure.getById(cache, session)("1", "2", "3")

  session.close()
  cluster.close()
}
/*
  object WeatherStation {

    def getById(cache: LoadingCache[String, PreparedStatement], session: Session)(ids: Integer*): List[Int] = {
      val query: Statement =
        QueryBuilder.select().
          all().
          from("quill_test", "encodingtestentity").
          where(QueryBuilder.in("id", ids.map(_ => QueryBuilder.bindMarker()): _*))

      session.execute(cache.get(query.toString).bind(ids: _*)).
        iterator().asScala.
        map { row => row.getInt("id") }.
        map { a => println(a); a }.
        to[List]
    }
  }

  WeatherStation.getById(cache, session)(1, 2, 3)

  session.close()
  cluster.close()
 */
/*
  object WeatherStation {

    def getAllByCountry(cache: LoadingCache[String, PreparedStatement], session: Session)(countries: String*): List[WeatherStation] = {
      val query: Statement =
        QueryBuilder.select().
          all().
          from("db", "weather_station").
          where(QueryBuilder.in("country", countries.map(_ => QueryBuilder.bindMarker()): _*))

      session.execute(cache.get(query.toString).bind(countries: _*)).
        iterator().asScala.
        map {
          row => WeatherStation(row.getString("country"), row.getString("city"), row.getString("station_id"), row.getInt("entry"), row.getInt("value"))
        }.
        map { a => println(a); a }.
        to[List]
    }
  }

  WeatherStation.getAllByCountry(cache, session)("ES", "UK", "US")

  session.close()
  cluster.close()
 */ 