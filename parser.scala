import com.mongodb.casbah.Imports._

object main
{
  def csvParserLine(s: String, res: Vector[String] = Vector(), tmp: String = "", c: Int = 0, quote: Boolean = false): List[String] = (s,quote) match {
    case (s, _) if c == s.length() => (res :+ tmp).toList.filter{x => x != ""} //we are in the end of the line, thus we add the tmp accumulator to the res vector
    case (s, false) if s(c) == '"' => csvParserLine(s, res, tmp + "\"", c + 1, true) // We have found an '"', now we are in "quote mode" activate
    case (s, true) if s(c) == '"'  => csvParserLine(s, res :+ (tmp + "\""), "", c + 1, false) //We have found the end of the string, thus we add this to the res vector and we reset the tmp accumulator
    case (s, false) if s(c) == ',' => csvParserLine(s, res :+ tmp, "", c + 1, false)  //We have found a comma not in a string, thus we can add the tmp accumulator to the res vector
    case _ => csvParserLine(s, res, tmp + s(c), c + 1, quote) // Add the char to the tmp accumulator and next char
  }

  def parseLines(l : List[String]): List[List[String]] = l.map{x => csvParserLine(x)}.toList

  def exists(l : List[String], i : Int) = if (i < l.length) Some(l(i).replaceAll("\"","")) else None

  def addCountriesCollection(c: MongoCollection): Unit = {
    if (c.count() == 0)
      parseLines(scala.io.Source.fromFile("countries.csv")("UTF-8").mkString.split("\n").toList).foreach { l =>
        c.insert(MongoDBObject("id" -> exists(l, 0),
          "code" -> exists(l, 1), "name" -> exists(l, 2), "continent" -> exists(l, 3),
          "wikipedia_link" -> exists(l, 4), "keywords" -> exists(l, 5)))
      }
  }

  def addRunwaysCollection(c: MongoCollection): Unit = {
    if (c.count() == 0)
      parseLines(scala.io.Source.fromFile("runways.csv")("UTF-8").mkString.split("\n").toList).foreach { l =>
        c.insert(MongoDBObject("id" -> exists(l, 0), "airport_ref" -> exists(l, 1), "airport_ident" -> exists(l, 2),
          "length_ft" -> exists(l, 3), "width_ft" -> exists(l, 4), "surface" -> exists(l, 5), "lighted" -> exists(l, 6),
          "closed" -> exists(l, 7), "le_ident" -> exists(l, 8), "le_latitude_deg" -> exists(l, 9),"le_longitude_deg" -> exists(l, 10),
          "le_elevation_ft" -> exists(l, 11),"le_heading_degT" -> exists(l, 12),"le_displaced_threshold_ft" -> exists(l, 13),
          "he_ident" -> exists(l, 14),"he_latitude_deg" -> exists(l, 15),"he_longitude_deg" -> exists(l, 16),"he_elevation_ft" -> exists(l, 17),
          "he_heading_degT" -> exists(l, 18), "he_displaced_threshold_ft" -> exists(l, 19)))
      }
  }

  def addAirportsCollection(c: MongoCollection): Unit = {
    if (c.count() == 0)
      parseLines(scala.io.Source.fromFile("airports.csv")("UTF-8").mkString.split("\n").toList).foreach { l =>
        c.insert(MongoDBObject("id" -> exists(l, 0), "ident" -> exists(l, 1), "type" -> exists(l, 2),
          "name" -> exists(l, 3), "latitude_deg" -> exists(l, 4), "longitude_deg" -> exists(l, 5), "elevation_ft" -> exists(l, 6),
          "continent" -> exists(l, 7), "iso_country" -> exists(l, 8), "iso_region" -> exists(l, 9),"municipality" -> exists(l, 10),
          "scheduled_service" -> exists(l, 11),"gps_code" -> exists(l, 12),"iata_code" -> exists(l, 13),
          "local_code" -> exists(l, 14),"home_link" -> exists(l, 15),"wikipedia_link" -> exists(l, 16),"keywords" -> exists(l, 17)))
      }
  }

  def addRunwaysAirportsCollection(c: MongoCollection, airports: List[List[String]]): Unit = {
    if (c.count() == 0)
      parseLines(scala.io.Source.fromFile("runways.csv")("UTF-8").mkString.split("\n").toList).foreach { l =>
        val t = airports.filter{x => x(0) == l(1)}.flatten
        c.insert(MongoDBObject("id" -> exists(l, 0), "airport_ref" -> exists(l, 1), "airport_ident" -> exists(l, 2),
          "length_ft" -> exists(l, 3), "width_ft" -> exists(l, 4), "surface" -> exists(l, 5), "lighted" -> exists(l, 6),
          "closed" -> exists(l, 7), "le_ident" -> exists(l, 8), "le_latitude_deg" -> exists(l, 9),"le_longitude_deg" -> exists(l, 10),
          "le_elevation_ft" -> exists(l, 11),"le_heading_degT" -> exists(l, 12),"le_displaced_threshold_ft" -> exists(l, 13),
          "he_ident" -> exists(l, 14),"he_latitude_deg" -> exists(l, 15),"he_longitude_deg" -> exists(l, 16),"he_elevation_ft" -> exists(l, 17),
          "he_heading_degT" -> exists(l, 18), "he_displaced_threshold_ft" -> exists(l, 19), "ident" -> exists(t, 1),
          "type" -> exists(t, 2), "name" -> exists(t, 3), "iso_country" -> exists(t, 8)))
      }
  }
  def main(args: Array[String]): Unit = {

    val mongoClient = MongoClient("localhost", 27017)("Airport")
    val countriesCollection = mongoClient("countries")
    addCountriesCollection(countriesCollection)

    val runwaysCollection = mongoClient("runways")
    runwaysCollection.createIndex("airport_ref")
    addRunwaysCollection(runwaysCollection)

    val airportsCollection = mongoClient("airports")
    airportsCollection.createIndex("id")
    addAirportsCollection(airportsCollection)

    //val t = parseLines(scala.io.Source.fromFile("airports.csv")("UTF-8").mkString.split("\n").toList)
    //val runwaysAirportsCollection = mongoClient("runways_airports")
    //runwaysAirportsCollection.createIndex("airport_ref")
    //addRunwaysAirportsCollection(runwaysAirportsCollection, t)

    //airportsCollection.foreach{x => println(x("name")+ ":" + runwaysAirportsCollection.find(MongoDBObject("iso_country" -> "FR", "airport_ref" -> x("id"))).count)}
  /*  val input = "FR"
    val list = countriesCollection.find(MongoDBObject("name" -> input)).toList
    val code = if (!list.isEmpty) list(0)("code") else (input)
    val interestingAirports = airportsCollection.find(MongoDBObject("iso_country" -> code))
*/
    val aggregationOptions = AggregationOptions(AggregationOptions.CURSOR)
    val results = airportsCollection.aggregate(
    List(
     MongoDBObject("$match" -> MongoDBObject("iso_country" -> "US")),
     MongoDBObject("$lookup" ->
     MongoDBObject("from" -> "runways", "localField" -> "id", "foreignField" -> "airport_ref", "as" -> "runways")),
     MongoDBObject("$unwind" -> "$runways")),
     aggregationOptions)

    println(results.foreach{x => println(x)})
    //airportsCollection.foreach{x => println(x("name") + ":" + results.toList.filter{t => t("airport_ref") == x("id")}.size)}
    //interestingAirports.foreach { a => println(a("name"))
    //println(runwaysCollection.find(MongoDBObject("airport_ref" -> a("id"))).size)
    //}
  }
}
//def test(t: String = ""): Any = scala.io.StdIn.readLine() match {
//  case ("Query") => println("query")
//  case ("Reports") => println("report")
//  case _ => println("Unknown Option")
//}
