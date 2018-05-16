import com.mongodb.casbah.Imports._
import parser._
import collection._

package airport_function
{
  object query_functions {
    /* Airport and number of their runways */
    def displayAirportsRunways(airportCollection: MongoCollection, runwaysCollection: MongoCollection, countryCode: AnyRef): Unit = {
      val interestingAirports = airportCollection.find(MongoDBObject("iso_country" -> countryCode))
      interestingAirports.foreach{airport => print(airport("name") + " ")
        println(runwaysCollection.find(MongoDBObject("airport_ref" -> airport("id"))).size)}
    }
  }

  /* Highest number of airports */
  object reports_functions {
    def rankNumberAirports(airportsCollection: MongoCollection, countriesCollection: MongoCollection): Unit = {
      val aggregationOptions = AggregationOptions(AggregationOptions.CURSOR)
      val tenFirstCountries = airportsCollection.aggregate(
        List(
          MongoDBObject("$group" -> MongoDBObject("_id" -> "$iso_country", "numbers" -> MongoDBObject{"$sum" -> 1})),
          MongoDBObject("$sort" -> MongoDBObject("numbers" -> -1)),
          MongoDBObject("$limit" -> 10)),
        aggregationOptions)
      tenFirstCountries.foreach{country => println(parser_anyref.giveCountryWithCode(country("_id"), countriesCollection) +
        " (" + country("_id") + ") => " + country("numbers"))}
    }

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
      typeRunways.foreach{runway => println(parser_anyref.giveCountryWithCode(runway("_id"), countriesCollection) + " (" +
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
      commonRunways.foreach{runway => println(runway("_id") + " => " + runway("numbers"))}
    }
  }
}
