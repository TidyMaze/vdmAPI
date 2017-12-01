package scraper

import db.StoriesDAO
import model.Story
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import parsers.VdmParser
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import scala.concurrent.Await

import scala.concurrent.duration._

class VdmScraper {
  val ROOT_URL = "http://www.viedemerde.fr"
  val MAX_STORIES = 200
  
  val parser = new VdmParser
  val browser = JsoupBrowser()
  
  val storiesDao = new StoriesDAO()
   
  def getUrlForPage(index: Int): String = s"$ROOT_URL/?page=$index"


  
  def run() = {
    println("Starting VDMApi scraping")
    val acc = scala.collection.mutable.ListBuffer.empty[Story]
    val it = new StoriesIterator(getUrlForPage, browser)
    while(it.hasNext && acc.length < MAX_STORIES){
        val currentStory = parser.extractStory(it.next())
        acc += currentStory
    }
    
    println("Samples extracted :")
    acc.slice(0, 5) foreach println
    
    println(s"Done with extracting $MAX_STORIES stories")
    println(s"Storing to DB")
    val futureAllInsertions = Future.sequence(acc.map(s => storiesDao.insert(s)))
    
    val allWork = for {
      resInsert <- futureAllInsertions
      resGetAll <- storiesDao.getAllStories
    } yield resGetAll
    
    allWork.onComplete {
      case Failure(t) => println("Error : " + t.getMessage)
      case Success(l) => {
        println("All inserted ! Exemples inserted :")
        l.slice(0, 5) foreach println
      }
    }
    Await. result(allWork, 5.minutes)
    println("Exiting VDMApi scraper")
  }
}