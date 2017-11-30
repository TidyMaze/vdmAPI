import tools.VdmDateParser
import org.scalatest.FlatSpec
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class VdmDateParserTest extends FlatSpec {

  val parser = new VdmDateParser()
  
  "Date parser" should "successfully parse a valid date" in {
    assert(parser.parse("jeudi 30 novembre 2017 17:00") == LocalDateTime.parse("2017-11-30T17:00:00"))
  }

  it should "produce DateTimeParseException when invalid date is given" in {
    assertThrows[DateTimeParseException] {
      parser.parse("jeudi 30 Saturn 10:65")
    }
  }
}