package parsers

import org.scalatest.FlatSpec
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import org.scalactic.source.Position.apply
import java.text.ParseException
import scraper.VdmParser

class VdmDateParserTest extends FlatSpec {

  val parser = new VdmParser()
  
  "Date parser" should "successfully parse a valid date" in {
    assert(parser.parseDate("jeudi 30 novembre 2017 17:00") == LocalDateTime.parse("2017-11-30T17:00:00"))
  }

  it should "produce DateTimeParseException when invalid date is given" in {
    assertThrows[DateTimeParseException] {
      parser.parseDate("jeudi 30 Saturn 10:65")
    }
  }
  
  "MetaParser" should "successfully parse a full meta row" in {
    assert(parser.parseMeta("Par Djo - / jeudi 30 novembre 2017 11:30 / France") == ("Djo", LocalDateTime.parse("2017-11-30T11:30")))
  }

  it should "produce ParseException when invalid meta is given" in {
    assertThrows[ParseException] {
      parser.parseMeta("this is some random text")
    }
  }
}