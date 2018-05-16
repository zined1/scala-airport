import com.mongodb.casbah.Imports._
import parser._
import collection._
import airport_function._

object main
{
  def menu(countriesCollection: MongoCollection, runwaysCollection: MongoCollection, airportsCollection: MongoCollection): Any = {
    println("----")
    println("1- Query")
    println("2- Reports")
    print("-> ")
    scala.io.StdIn.readLine() match {
      case "Query" | "1" => query_menu(countriesCollection, runwaysCollection, airportsCollection)
      case "Reports" | "2" => reports_menu(countriesCollection, runwaysCollection, airportsCollection)
      case _ => println("Unknown Option")
    }
  }

  def query_menu(countriesCollection: MongoCollection, runwaysCollection: MongoCollection, airportsCollection: MongoCollection): Any = {
    println("Enter the country or coutry_code (e.g: France or FR)")
    print("-> ")
    query_functions.displayAirportsRunways(airportsCollection, countriesCollection, runwaysCollection, scala.io.StdIn.readLine())
    menu(countriesCollection, runwaysCollection, airportsCollection)
  }

  def reports_menu(countriesCollection: MongoCollection, runwaysCollection: MongoCollection, airportsCollection: MongoCollection): Any = {
    println("1- Highest/Lowest number of airports")
    println("2- Type of runways")
    println("3- 10 most common runway latitude")
    print("-> ")
    scala.io.StdIn.readLine() match {
      case "1" => reports_functions.rankNumberAirports(airportsCollection, countriesCollection)
      case "2" => reports_functions.typeRunways(airportsCollection, countriesCollection)
      case "3" => reports_functions.commonRunways(runwaysCollection)
      case _ => println("Unknown Option")
    }
    menu(countriesCollection, runwaysCollection, airportsCollection)
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

    //val input = "France"
    //val list = countriesCollection.find(MongoDBObject("name" -> input)).toList
    //val countryCode = if (!list.isEmpty) list(0)("code") else (input)
    menu(countriesCollection, runwaysCollection, airportsCollection)
  }
}
