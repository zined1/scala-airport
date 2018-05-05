import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection

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

  def getColl(s : String): MongoCollection = MongoClient("localhost", 27017)("Airport")(s)

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

  def main(args: Array[String]): Unit = {
    val countriesCollection = getColl("countries")
    addCountriesCollection(countriesCollection)

    val runwaysCollection = getColl("runways")
    addRunwaysCollection(runwaysCollection)

    val airportsCollection = getColl("airports")
    addAirportsCollection(airportsCollection)
    val input = "FR"
    val list = countriesCollection.filter{ x => (x("name")) == input }
    val code = if (!list.isEmpty) list.last("code") else (input)
    val interstingCountries = countriesCollection.filter{x => x("code") == code}
    interstingCountries.foreach{
      x => airportsCollection.filter{a => a("iso_country") == x("code")}.foreach{ f => println(f("name")) }
    }
  }
}
//def test(t: String = ""): Any = scala.io.StdIn.readLine() match {
//  case ("Query") => println("query")
//  case ("Reports") => println("report")
//  case _ => println("Unknown Option")
//}
