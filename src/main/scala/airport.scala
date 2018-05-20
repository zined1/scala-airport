import com.mongodb.casbah.Imports._
import parser._
import collection._

package airport_function
{
  object query_functions {
    /* Airport and number of their runways */
    def displayAirportsRunways(airportsCollection: MongoCollection, countriesCollection : MongoCollection, runwaysCollection: MongoCollection, givenCode: AnyRef): Unit = {
      val countryCode = check_collection.getCodeWithCountryName(givenCode, countriesCollection)
      val interestingAirports = airportsCollection.find(MongoDBObject("iso_country" -> countryCode))
      if (interestingAirports.size != 0) {
        println(parser_anyref.giveCountryWithCode(countryCode, countriesCollection) + " ("+ countryCode +"):")
        interestingAirports.foreach {
          airport => print("     " + airport("name") + " ("+ airport("ident") + ") ")
          val runways = runwaysCollection.find(MongoDBObject("airport_ref" -> airport("id"))).toList
          val runwaysSize = runways.size
          if (runwaysSize != 0) {
            if (runwaysSize == 1) print("(1 runway) => ") else print(" (" + runwaysSize + " runways) => ")
            runways.foreach{runway =>
              print("[Surface: " + runway("surface") + ", Length(ft): " +
                runway("length_ft") + ", Width(ft): " + runway("width_ft") + "] | ")}
          }
          else print("(No runway)")
          println("")
        }
      }
      else println("No country found...")
    }
  }

  object reports_functions {

    /* Type of runways per country */
    def typeRunways(airportsCollection: MongoCollection, countriesCollection: MongoCollection): Unit = {
      val aggregationOptions = AggregationOptions(AggregationOptions.CURSOR)
      val typeRunways = airportsCollection.aggregate(
        List(
          MongoDBObject("$lookup" ->
            MongoDBObject("from" -> "runways", "localField" -> "id", "foreignField" -> "airport_ref", "as" -> "runways")),
          MongoDBObject("$unwind" -> "$runways"),
          MongoDBObject("$group" -> MongoDBObject("_id" -> "$iso_country",
            "surface" -> MongoDBObject("$addToSet" -> "$runways.surface"))),
          MongoDBObject("$sort" -> MongoDBObject("_id" -> 1))),
        aggregationOptions).toList
      typeRunways.foreach{runway =>
        println(parser_anyref.giveCountryWithCode(runway("_id"), countriesCollection) + " (" +
        runway("_id") + ") => " + parser_anyref.prettyPrintAnyRef(runway("surface")))}
    }

    /* 10 commons runways latitude */
    def commonRunways(runwaysCollection: MongoCollection): Unit = {
      val aggregationOptions = AggregationOptions(AggregationOptions.CURSOR)
      val commonRunways = runwaysCollection.aggregate(
        List(
          MongoDBObject("$group" -> MongoDBObject("_id" -> "$le_ident", "numbers" -> MongoDBObject{"$sum" -> 1})),
          MongoDBObject("$sort" -> MongoDBObject("numbers" -> -1)),
          MongoDBObject("$limit" -> 10)),
        aggregationOptions)
      commonRunways.foreach{runway => println(runway("_id") + " => " + runway("numbers") + " times")}
    }

    /* Highest number of airports */
    def rankNumberAirports(airportsCollection: MongoCollection, countriesCollection: MongoCollection): Unit = {
      val tenFirstCountries = countriesCollection.toList.map{countries =>
        List(countries("code").toString, airportsCollection.find(MongoDBObject("iso_country" -> countries("code"))).size.toInt)}.
        map{country => (country(0).toString, country(1).toString.toInt)}.sortBy(country => country._2)
      println("HIGHEST :")
      tenFirstCountries.takeRight(10).reverse.foreach{country =>
        println("    " + parser_anyref.giveCountryWithCode(country._1, countriesCollection) +
          " (" + country._1 + ") => " + country._2 + " airports")}

      println("LOWEST :")
      tenFirstCountries.take(10).foreach{country =>
        println("    " + parser_anyref.giveCountryWithCode(country._1, countriesCollection) +
          " (" + country._1 + ") => " + country._2 + " airport")}
    }
  }
}
