import com.mongodb.casbah.Imports._

package parser
{
  object parser_csv {
    // Parse the csv
    def csvParserLine(s: String, res: Vector[String] = Vector(), tmp: String = "", c: Int = 0, quote: Boolean = false): List[String] = (s,quote) match {
      case (s, _) if s.length == 0 => res.toList
      case (s, _) if c == s.length() && s(c - 1) == ',' => (res :+ "N/A").toList.filter{x => x != ""} //we are in the end of the line, thus if there is an last , we add "N\A"
      case (s, _) if c == s.length() => (res :+ tmp).toList.filter{x => x != ""} //we are in the end of the line, thus we add the tmp accumulator to the res vector
      case (s, false) if s(c) == '"' => csvParserLine(s, res, tmp + "\"", c + 1, true) // We have found an '"', now we are in "quote mode" activate
      case (s, true) if s(c) == '"' && c != (s.length() - 1) && s(c + 1) == ',' => csvParserLine(s, res :+ (tmp + "\""), "", c + 1, false) //We have found the end of the string, thus we add this to the res vector and we reset the tmp accumulator
      case (s, false) if s(c) == ',' && c != (s.length() - 1) && s(c + 1) == ','  => csvParserLine(s, res :+ tmp :+ "N/A", "", c + 1, false)  //We have found a comma followed by another comma, we add "N/A" to the vector
      case (s, false) if s(c) == ',' => csvParserLine(s, res :+ tmp, "", c + 1, false)  //We have found a comma not in a string and not follwed by another comma, thus we can add the tmp accumulator to the res vector
      case _ => csvParserLine(s, res, tmp + s(c), c + 1, quote) // Add the char to the tmp accumulator and next char
    }
    def fileToList(file: String): List[String] = scala.io.Source.fromFile(file)("UTF-8").mkString.split("\n").toList
  }

  object parser_countries {
    case class Countries(id: String, code: String, name: String, continent: String, wikipedia_link: String, keywords: String)
    def stringToCountries(l: List[String]): Countries = Countries(l(0), l(1), l(2), l(3), l(4), l(5))
    def parseLinesCountries(l : List[String]): List[Countries] = l.map{x => stringToCountries(parser_csv.csvParserLine(x))}.toList
  }

  object parser_airports {
    case class Airports(id: String, ident: String, `type`: String, name: String, latitude_deg: String, longitude_deg: String,
      elevation_ft: String, continent: String, iso_country: String, iso_region: String, municipality: String, scheduled_service: String,
      gps_code: String, iata_code: String, local_code: String, home_link: String, wikipedia_link: String, keywords: String)

    def parseLinesAirports(l : List[String]): List[Airports] = l.map{x => stringToAirports(parser_csv.csvParserLine(x))}.toList

    def stringToAirports(l: List[String]): Airports = Airports(l(0), l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9),
      l(10), l(11), l(12), l(13), l(14), l(15), l(16), l(17))
  }

  object parser_runways {
    case class Runways(id: String, airport_ref: String, airport_ident: String, length_ft: String, width_ft: String, surface: String,
      lighted: String, closed: String, le_ident: String, le_latitude_deg: String, le_longitude_deg: String, le_elevation_ft: String,
      le_heading_degT: String, le_displaced_threshold_ft: String, he_ident: String, he_latitude_deg: String, he_longitude_deg: String,
      he_elevation_ft: String, he_heading_degT: String, he_displaced_threshold_ft: String)

    def parseLinesRunways(l : List[String]): List[Runways] = l.map{x => stringToRunways(parser_csv.csvParserLine(x))}.toList

    def stringToRunways(l: List[String]): Runways = Runways(l(0), l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9),l(10),
      l(11), l(12), l(13), l(14), l(15), l(16), l(17), l(18), l(19))
  }

  object parser_anyref {
    // Transform the Anyref sent by Casbah in order to have a PrettyPrint
    def prettyPrintAnyRef(s: AnyRef): String = s.toString.replace(" , ", " | ").dropRight(1).drop(2)
    // Give the country with the code (e.g. FR => France)
    def giveCountryWithCode(code: AnyRef, collection: MongoCollection): String =
      collection.find(MongoDBObject("code" -> code)).toList(0)("name").toString
  }
}
