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
import java.time.ZonedDateTime
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZoneId

class VdmParser {
  val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy HH:mm").withLocale(Locale.FRENCH);
  
  def extractStory(el: Element): Story = {
    val content = el >?> text("> div > div > div.panel-content > p > a")
    val meta = el >> text("div.text-center")
    val (author, date) = parseMeta(meta)
    Story(None, content, author, date)
  }
  
  // Assuming the date one website is in Paris Zone (GMT +1)
  def parseDate(dateStr: String): Instant = ZonedDateTime.of(LocalDateTime.parse(dateStr, formatter), ZoneId.of("Europe/Paris")).toInstant();
  
  def parseMeta(metaStr: String): (String, Instant) = {
    val metaSplit = metaStr.split("/");
    if (metaSplit.length < 2) throw new ParseException("There are less than 2 parts in meta : " + metaStr, 0);
    val author = "Par (.*) - ".r.findFirstMatchIn(metaSplit(0)).map(m => m.group(1))
      .getOrElse("Unknown user")
    val date = parseDate(metaSplit(1).trim())
    (author, date)
  }
}