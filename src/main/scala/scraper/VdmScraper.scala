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
import java.time.LocalDateTime

class VdmScraper {
  val ROOT_URL = "http://www.viedemerde.fr"
  val MAX_STORIES = 200

  val parser = new VdmParser
  val browser = JsoupBrowser()

  val storiesDao = new StoriesDAO()

  def getUrlForPage(index: Int): String = s"$ROOT_URL/?page=$index"

  def scrapeAndStoreToDb(latestStoryDate: Option[LocalDateTime]) = {
    println(s"Storing to DB")
    Future.sequence(scrapeStories(latestStoryDate).map(s => storiesDao.insert(s)))
  }
  
  def run() = {
    println("Starting VDMApi scraping")
    
    val allWork = for {
      latestStoryDate <- storiesDao.getLatestStory.map(_.map(_.date))
      resInsert <- scrapeAndStoreToDb(latestStoryDate)
      resGetAll <- storiesDao.getAllStories
    } yield resGetAll
    
    allWork.onComplete {
      case Failure(t) => println("Error : " + t.getMessage)
      case Success(l) => {
        println("All inserted ! New DB samples :")
        l.slice(0, 5) foreach println
      }
    }

    Await.result(allWork, 5.minutes)
    println("Exiting VDMApi scraper")
  }

  def scrapeStories(latest: Option[LocalDateTime]) = {
    val acc = scala.collection.mutable.ListBuffer.empty[Story]
    val it = new StoriesIterator(getUrlForPage, browser)
    var reachedAlreadyParsed = false
    while (it.hasNext && acc.length < MAX_STORIES && !reachedAlreadyParsed) {
      val currentStory = parser.extractStory(it.next())
      if(latest.forall(currentStory.date.isAfter(_))) acc += currentStory
      else reachedAlreadyParsed = true
    }
    println(s"Done with extracting up to $MAX_STORIES stories :  ${acc.length}")
    println("Samples extracted :")
    acc.slice(0, 5) foreach println
    acc.toList
  }
}