import org.scalatest._
import parser._

class TestParserCSV extends FunSuite with Matchers {
  test("Empty string") {
    val data = parser_csv.csvParserLine("")
    data should be (List())
  }

  test("Simple string") {
    val data = parser_csv.csvParserLine("salut")
    data should be (List("salut"))
  }

  test("Simple CSV without comma inside quote") {
    val data = parser_csv.csvParserLine("\"foo\",1337,\"bar\"")
    data should be (List("\"foo\"","1337","\"bar\""))
  }

  test("CSV with comma inside quote") {
    val data = parser_csv.csvParserLine("\"foo,bar\",2,\"again, with space\"")
    data should be (List("\"foo,bar\"","2","\"again, with space\""))
  }

  test("CSV with quote and comma inside quote") {
    val data = parser_csv.csvParserLine("\"Fazenda São José \"\"OB\"\" Airport\",-21.425199508666992")
    data should be (List("\"Fazenda São José \"\"OB\"\" Airport\"", "-21.425199508666992"))
  }

  test("CSV with somes elements null in the middle") {
    val data = parser_csv.csvParserLine("\"foo\",,,,3")
    data should be (List("\"foo\"","N/A", "N/A", "N/A", "3"))
  }

  test("CSV with element null at the end") {
    val data = parser_csv.csvParserLine("salut,,,,,")
    data should be (List("salut","N/A","N/A","N/A","N/A","N/A"))
  }
}

class TestParserCountries extends FunSuite with Matchers {
  test("StringToCountries") {
    val data = parser_countries.stringToCountries(List("302637","IR","Iran","AS","http://en.wikipedia.org/wiki/Iran","N/A"))
    data should be (parser_countries.Countries("302637", "IR", "Iran", "AS", "http://en.wikipedia.org/wiki/Iran","N/A"))
  }
}

class TestParserAirports extends FunSuite with Matchers {
  test("StringToAirports with N/A elements in the middle") {
    val data = parser_airports.stringToAirports(List("29929","LFPR","closed","Guyancourt Airport","48.76029968261719",
      "2.0625","541","EU","FR","FR-J","Guyancourt","no","LFPR","N/A","N/A","N/A","http://fr.wikipedia.org/wiki/A%C3%A9rodrome_de_Guyancourt","N/A"))
    data should be (parser_airports.Airports("29929","LFPR","closed","Guyancourt Airport","48.76029968261719","2.0625",
      "541","EU","FR","FR-J","Guyancourt","no","LFPR","N/A","N/A","N/A","http://fr.wikipedia.org/wiki/A%C3%A9rodrome_de_Guyancourt","N/A"))
  }
}

class TestParserRunways extends FunSuite with Matchers {
  test("StringToRunways with N/A elements in the middle") {
    val data = parser_runways.stringToRunways(List("257922","16935","CO82","1200","20","CONC","0","0","08","N/A","N/A",
      "N/A","N/A","N/A","26","N/A","N/A","N/A","N/A","N/A"))
    data should be (parser_runways.Runways("257922","16935","CO82","1200","20","CONC","0","0","08","N/A","N/A","N/A",
      "N/A","N/A","26","N/A","N/A","N/A","N/A","N/A"))
  }
}
