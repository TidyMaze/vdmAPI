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

class VdmScraper {
  val ROOT_URL = "http://www.viedemerde.fr"
  val MAX_STORIES = 200
  
  val metaPaser = new VdmMetaParser
   val browser = JsoupBrowser()
  
  def getUrlForPage(index: Int): String = s"$ROOT_URL/?page=$index"
  
  def run() = {
    println("Starting VDMApi scraping")
    
    val currentStories = scala.collection.mutable.ListBuffer.empty[Story]
    
    val storiesIterator = new StoriesIterator(getUrlForPage, browser)
    
    while(storiesIterator.hasNext && currentStories.length < MAX_STORIES){
        currentStories += extractStory(storiesIterator.next())
    }
    currentStories foreach println
    println("Done with extracting content")
    
  }

  def extractStory(el: Element): Story = {
    val content = el >?> text("> div > div > div.panel-content > p > a")
    val meta = el >> text("div.text-center")
    val (author, date) = metaPaser.parse(meta)
    Story(content, date, author)
  }
}