package scraper

import db.StoriesDAO
import model.Story
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import parsers.VdmParser
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

class VdmScraper {
  val ROOT_URL = "http://www.viedemerde.fr"
  val MAX_STORIES = 200
  
  val parser = new VdmParser
   val browser = JsoupBrowser()
  
  def getUrlForPage(index: Int): String = s"$ROOT_URL/?page=$index"
  
  def run() = {
    println("Starting VDMApi scraping")
    val acc = scala.collection.mutable.ListBuffer.empty[Story]
    val it = new StoriesIterator(getUrlForPage, browser)
    while(it.hasNext && acc.length < MAX_STORIES){
        val currentStory = parser.extractStory(it.next())
        acc += currentStory
    }
    acc foreach println
    println(s"Done with extracting $MAX_STORIES stories")
    
    val storiesDao = new StoriesDAO()
    println("Stored stories :")
    val storiesFuture = storiesDao.getAllStories
    storiesFuture.onFailure { case t => println(t.getMessage) }
    storiesFuture foreach { println }
  }  
}