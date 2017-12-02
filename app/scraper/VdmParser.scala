package scraper

import java.util.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.ParseException
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import model.Story

class VdmParser {
  val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy HH:mm").withLocale(Locale.FRENCH);
  
  def extractStory(el: Element): Story = {
    val content = el >?> text("> div > div > div.panel-content > p > a")
    val meta = el >> text("div.text-center")
    val (author, date) = parseMeta(meta)
    Story(None, content, author, date)
  }
  
  def parseDate(dateStr: String): LocalDateTime = LocalDateTime.parse(dateStr, formatter);
  
  def parseMeta(metaStr: String): (String, LocalDateTime) = {
    val metaSplit = metaStr.split("/");
    if (metaSplit.length < 2) throw new ParseException("There are less than 2 parts in meta : " + metaStr, 0);
    val author = "Par (.*) - ".r.findFirstMatchIn(metaSplit(0)).map(m => m.group(1))
      .getOrElse("Unknown user")
    val date = parseDate(metaSplit(1).trim())
    (author, date)
  }
}