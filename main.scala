import com.mongodb.casbah.Imports._
import parser._
import collection._
import airport_function._

object main
{

  def menu(): Any = scala.io.StdIn.readLine() match {
    case ("Query") => println("query")
    case ("Reports") => println("report")
    case _ => println("Unknown Option")
  }

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val countriesCollection = mongoClient("countries")
    insert_collection.insertCountriesCollection(countriesCollection, "countries.csv")

    val runwaysCollection = mongoClient("runways")
    runwaysCollection.createIndex("airport_ref")
    insert_collection.insertRunwaysCollection(runwaysCollection, "runways.csv")

    val airportsCollection = mongoClient("airports")
    airportsCollection.createIndex("id")
    insert_collection.insertAirportsCollection(airportsCollection, "airports.csv")

    val input = "France"
    val list = countriesCollection.find(MongoDBObject("name" -> input)).toList
    val countryCode = if (!list.isEmpty) list(0)("code") else (input)

    query_functions.displayAirportsRunways(airportsCollection, runwaysCollection, countryCode)

    reports_functions.rankNumberAirports(airportsCollection, countriesCollection)
    reports_functions.typeRunways(airportsCollection, countriesCollection)
    reports_functions.commonRunways(runwaysCollection)
  }
}
