import parsers.VdmDateParser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Element

import net.ruippeixotog.scalascraper.browser.{ HtmlUnitBrowser, JsoupBrowser }
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.HtmlValidator
import net.ruippeixotog.scalascraper.util.EitherRightBias._
import net.ruippeixotog.scalascraper.model.ElementQuery
import java.time.LocalDateTime
import parsers.VdmMetaParser
import db.StoriesDAO

class VdmScraper {
  val ROOT_URL = "http://www.viedemerde.fr"
  val MAX_STORIES = 200
  
  val metaPaser = new VdmMetaParser
   val browser = JsoupBrowser()
  
  def getUrlForPage(index: Int): String = s"$ROOT_URL/?page=$index"
  
  def run() = {
    println("Starting VDMApi scraping")
    val acc = scala.collection.mutable.ListBuffer.empty[Story]
    val it = new StoriesIterator(getUrlForPage, browser)
    while(it.hasNext && acc.length < MAX_STORIES){
        val currentStory = extractStory(it.next())
        acc += currentStory
    }
    acc foreach println
    println(s"Done with extracting $MAX_STORIES stories")
    
    val storiesDao = new StoriesDAO()
    storiesDao.getAllStories
  }

  def extractStory(el: Element): Story = {
    val content = el >?> text("> div > div > div.panel-content > p > a")
    val meta = el >> text("div.text-center")
    val (author, date) = metaPaser.parse(meta)
    Story(content, date, author)
  }
}