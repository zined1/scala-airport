import org.scalatest._
import com.mongodb.casbah.Imports._
import collection._

class TestInsertCollectionCountries extends FunSuite with Matchers {
  test("Insert Countries and check number") {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val countriesCollection = mongoClient("countries")
    countriesCollection.drop()
    insert_collection.insertCountriesCollection(countriesCollection, "./src/main/ressources/countries.csv")
    countriesCollection.size should equal (247)
  }
  test("Check if Countries is good")
  {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val countriesCollection = mongoClient("countries")
    val noCountry = countriesCollection.find(MongoDBObject("name" -> "Unknown or unassigned country")).toList
    noCountry(0)("continent") should equal ("AF")
  }
}

class TestInsertCollectionAirports extends FunSuite with Matchers {
  test("Insert Airports and check number") {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val airportsCollection = mongoClient("airports")
    airportsCollection.drop()
    insert_collection.insertAirportsCollection(airportsCollection, "./src/main/ressources/airports.csv")
    airportsCollection.size should equal (46505)
  }
  test("Check if Airports collection is good")
  {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val airportsCollection = mongoClient("airports")
    val cimedHeliport = airportsCollection.find(MongoDBObject("ident" -> "SDOH")).toList
    cimedHeliport(0)("name") should equal ("Cimed Heliport")
  }
  test("Check if number of small_airport is good")
  {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val airportsCollection = mongoClient("airports")
    val numberSmallAirport = airportsCollection.find(MongoDBObject("type" -> "small_airport")).size
    numberSmallAirport should equal (29939)
  }
}

class TestInsertCollectionRunways extends FunSuite with Matchers {
  test("Insert Runways and check number") {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val runwaysCollection = mongoClient("runways")
    runwaysCollection.drop()
    insert_collection.insertRunwaysCollection(runwaysCollection, "./src/main/ressources/runways.csv")
    runwaysCollection.size should equal (39536)
  }
  test("Check if Runways with empty elements collection is good")
  {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val runwaysCollection = mongoClient("runways")
    val runway = runwaysCollection.find(MongoDBObject("airport_ref" -> "313629")).toList
    runway(0)("airport_ident") should equal ("ZZZZ")
  }
  test("Check if empty element is null in the database")
  {
    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val runwaysCollection = mongoClient("runways")
    val numberSmallAirport = runwaysCollection.find(MongoDBObject("id" -> "255155")).toList
    numberSmallAirport(0)("le_longitude_deg") should equal (None)
  }
}
