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

  def main(args: Array[String]): Unit = {
    println("Hello World!")
  }
}
//def test(t: String = ""): Any = scala.io.StdIn.readLine() match {
//  case ("Query") => println("query")
//  case ("Reports") => println("report")
//  case _ => println("Unknown Option")
//}
