import java.io.PrintStream
import java.net.{ InetSocketAddress, Proxy }

import scala.collection.immutable.SortedMap

import net.ruippeixotog.scalascraper.browser.{ HtmlUnitBrowser, JsoupBrowser }
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.HtmlValidator
import net.ruippeixotog.scalascraper.util.EitherRightBias._
import net.ruippeixotog.scalascraper.model.ElementQuery
import java.util.Calendar
import java.util.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import tools.VdmDateParser
import tools.VdmDateParser
import tools.VdmDateParser

object Main extends App {
  
  val ROOT_URL = "http://www.viedemerde.fr"
  
  val dateParser = new VdmDateParser;
  
  println("Starting VDMApi scraping")
  val browser = JsoupBrowser()
  val doc = browser.get(ROOT_URL)
  val articlesDOM = doc >> elementList("#content > div > div.col-sm-8 > div.row .art-panel")
  val articles = articlesDOM map extractVDM

  def extractVDM(el: Element): Story = {
    val content = el >?> text("> div > div > div.panel-content > p > a")
    val meta = el >> text("div.text-center")
    println(meta)
    
    val metaSplit = meta.split("/");
    val author = "Par (.*) - ".r.findFirstMatchIn(metaSplit(0)).map(m => m.group(1)).getOrElse("Unknown user")
    val date = dateParser.parse(metaSplit(1).trim())
    
    val res = Story(content, date, author)
    println(res)
    return res
  }
}
