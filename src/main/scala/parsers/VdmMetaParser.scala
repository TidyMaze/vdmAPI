package parsers

import java.time.LocalDateTime
import java.text.ParseException

class VdmMetaParser {
  val dateParser = new VdmDateParser;
  
  def parse(metaStr: String): (String, LocalDateTime) = {
    val metaSplit = metaStr.split("/");
    if(metaSplit.length < 2) throw new ParseException("There are less than 2 parts in meta : " + metaStr, 0);
    val author = "Par (.*) - ".r.findFirstMatchIn(metaSplit(0)).map(m => m.group(1))
      .getOrElse("Unknown user")
    val date = dateParser.parse(metaSplit(1).trim())
    (author, date)
  }
}
