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
    println("3- Exit")
    print("-> ")
    scala.io.StdIn.readLine() match {
      case "Query" | "1" => query_menu(countriesCollection, runwaysCollection, airportsCollection)
      case "Reports" | "2" => reports_menu(countriesCollection, runwaysCollection, airportsCollection)
      case "Exit" | "3" => println("Exit...")
      case _ => println("Unknown Option")
      menu(countriesCollection, runwaysCollection, airportsCollection)
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
    val path = "./src/main/ressources/"
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val countriesCollection = mongoClient("countries")
    countriesCollection.createIndex("code")
    insert_collection.insertCountriesCollection(countriesCollection, path + "countries.csv")

    val airportsCollection = mongoClient("airports")
    airportsCollection.createIndex("id")
    airportsCollection.createIndex("iso_country")
    insert_collection.insertAirportsCollection(airportsCollection, path + "airports.csv")

    val runwaysCollection = mongoClient("runways")
    runwaysCollection.createIndex("airport_ref")
    insert_collection.insertRunwaysCollection(runwaysCollection, path + "runways.csv")

    menu(countriesCollection, runwaysCollection, airportsCollection)
  }
}
