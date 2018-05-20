import com.mongodb.casbah.Imports._
import parser._

package collection
{
  object insert_collection {
    // Add countries into a collection
    def insertCountriesCollection(collection: MongoCollection, file: String): Unit = {
      if (collection.count() == 0) {
        println("Insert Countries collection...")
        parser_countries.parseLinesCountries(parser_csv.fileToList(file)).drop(1).foreach { country =>
          collection.insert(MongoDBObject(
            "id" -> check_collection.exists(country.id),
            "code" -> check_collection.exists(country.code),
            "name" -> check_collection.exists(country.name),
            "continent" -> check_collection.exists(country.continent),
            "wikipedia_link" -> check_collection.exists(country.wikipedia_link),
            "keywords" -> check_collection.exists(country.keywords)))
        }
      }
    }

    // Add runways into a collection
    def insertRunwaysCollection(collection: MongoCollection, file: String): Unit = {
      if (collection.count() == 0) {
        println("Insert Runways collection...")
        parser_runways.parseLinesRunways(parser_csv.fileToList(file)).drop(1).foreach { runway =>
          collection.insert(MongoDBObject(
            "id" -> check_collection.exists(runway.id),
            "airport_ref" -> check_collection.exists(runway.airport_ref),
            "airport_ident" -> check_collection.exists(runway.airport_ident),
            "length_ft" -> check_collection.exists(runway.length_ft),
            "width_ft" -> check_collection.exists(runway.width_ft),
            "surface" -> check_collection.exists(runway.surface),
            "lighted" -> check_collection.exists(runway.lighted),
            "closed" -> check_collection.exists(runway.closed),
            "le_ident" -> check_collection.exists(runway.le_ident),
            "le_latitude_deg" -> check_collection.exists(runway.le_latitude_deg),
            "le_longitude_deg" -> check_collection.exists(runway.le_longitude_deg),
            "le_elevation_ft" -> check_collection.exists(runway.le_elevation_ft),
            "le_heading_degT" -> check_collection.exists(runway.le_heading_degT),
            "le_displaced_threshold_ft" -> check_collection.exists(runway.le_displaced_threshold_ft),
            "he_ident" -> check_collection.exists(runway.he_ident),
            "he_latitude_deg" -> check_collection.exists(runway.he_latitude_deg),
            "he_longitude_deg" -> check_collection.exists(runway.he_longitude_deg),
            "he_elevation_ft" -> check_collection.exists(runway.he_elevation_ft),
            "he_heading_degT" -> check_collection.exists(runway.he_heading_degT),
            "he_displaced_threshold_ft" -> check_collection.exists(runway.he_displaced_threshold_ft)))
        }
      }
    }

    // Add airports into collection
    def insertAirportsCollection(collection: MongoCollection, file: String): Unit = {
      if (collection.count() == 0) {
        println("Insert Airports collection...")
        parser_airports.parseLinesAirports(parser_csv.fileToList(file)).drop(1).foreach { airport =>
          collection.insert(MongoDBObject(
            "id" -> check_collection.exists(airport.id),
            "ident" -> check_collection.exists(airport.ident),
            "type" -> check_collection.exists(airport.`type`),
            "name" -> check_collection.exists(airport.name),
            "latitude_deg" -> check_collection.exists(airport.latitude_deg),
            "longitude_deg" -> check_collection.exists(airport.longitude_deg),
            "elevation_ft" -> check_collection.exists(airport.elevation_ft),
            "continent" -> check_collection.exists(airport.continent),
            "iso_country" -> check_collection.exists(airport.iso_country),
            "iso_region" -> check_collection.exists(airport.iso_region),
            "municipality" -> check_collection.exists(airport.municipality),
            "scheduled_service" -> check_collection.exists(airport.scheduled_service),
            "gps_code" -> check_collection.exists(airport.gps_code),
            "iata_code" -> check_collection.exists(airport.iata_code),
            "local_code" -> check_collection.exists(airport.local_code),
            "home_link" -> check_collection.exists(airport.home_link),
            "wikipedia_link" -> check_collection.exists(airport.wikipedia_link),
            "keywords" -> check_collection.exists(airport.keywords)))
        }
      }
    }
  }
  object check_collection {
    // Check if the elements exists in order to add in the database
    def exists(element: String) = if (element == "N/A") None else element.replaceAll("\"","")
    // Searches for country code if a country name was given
    def getCodeWithCountryName(code : AnyRef, collection : MongoCollection) = {
      val list = collection.filter{ country => country("name").
        toString.toLowerCase.startsWith(code.toString.toLowerCase)}.toList
      if (!list.isEmpty) list(0)("code") else (code)
    }
  }
}
