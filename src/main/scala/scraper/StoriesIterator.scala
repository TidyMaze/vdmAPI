package scraper

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.util.EitherRightBias._

class StoriesIterator(getPageUrlFn: Int => String, browser: Browser) extends Iterator[Element] {
  
  val STORIES_PATH = "#content > div > div.col-sm-8 > div.row .art-panel"
  
  var currentPage: Int = 0
  var currentInPage: Int = -1
  var currentPageStories: List[Element] = List.empty[Element]
  var nextPageStories: List[Element] = List.empty[Element]
  
  def getStoriesElementsInPage(indexPage: Int): List[Element] = browser.get(getPageUrlFn(indexPage)) >> elementList(STORIES_PATH)
  
  /**
   * Satisfies the Itarator contract by preloading next page if it's the last item
   */
  def hasNext: Boolean = {
    // TODO avoid multiple loading on several hasNext() when on the last item of page
    if((currentPage == 0 && nextPageStories.isEmpty) || (currentInPage == currentPageStories.length-1)){
      nextPageStories = getStoriesElementsInPage(currentPage + 1)
      return nextPageStories.nonEmpty
    }
    return true
  }

  def next(): Element = {
    if(currentPage == 0 || currentInPage == currentPageStories.length-1){
      // we reuse already preloaded page
      currentPageStories = nextPageStories
      currentInPage = 0
      currentPage += 1
    } else {
      // next is next item in current page
      currentInPage += 1
    } 
    return currentPageStories(currentInPage)
  }
}