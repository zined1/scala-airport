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

  def getColl(s : String) = MongoClient("localhost", 27017)("Airport")(s)
  
  def exists(l : List[String], i : Int) =
    if (i < l.length) Some(l(i)) else None

  def main(args: Array[String]): Unit = {
    parseLines(scala.io.Source.fromFile("countries.csv")("UTF-8").mkString.split("\n").toList).foreach { l => 
      getColl("countries").insert(MongoDBObject("id" -> exists(l, 0),
        "code" -> exists(l, 1),
        "name" -> exists(l, 2),
        "continent" -> exists(l, 3),
        "wikipedia_link" -> exists(l, 4),
        "keywords" -> exists(l,5)))
    }
  }
}
//def test(t: String = ""): Any = scala.io.StdIn.readLine() match {
//  case ("Query") => println("query")
//  case ("Reports") => println("report")
//  case _ => println("Unknown Option")
//}
