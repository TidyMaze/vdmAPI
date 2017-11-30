package parsers

import org.scalatest.FlatSpec
import java.time.LocalDateTime
import java.text.ParseException
import org.scalactic.source.Position.apply

class VdmMetaParserTest extends FlatSpec {

  val parser = new VdmMetaParser()
  
  "MetaParser" should "successfully parse a full meta row" in {
    assert(parser.parse("Par Djo - / jeudi 30 novembre 2017 11:30 / France") == ("Djo", LocalDateTime.parse("2017-11-30T11:30")))
  }

  it should "produce ParseException when invalid meta is given" in {
    assertThrows[ParseException] {
      parser.parse("this is some random text")
    }
  }
}