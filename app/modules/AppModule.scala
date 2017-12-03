
package modules

import db.StoriesDAO
import play.api.inject.Module
import play.api.Environment
import play.api.Configuration
import com.google.inject.Singleton
import scala.collection.Seq

class AppModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    Seq(bind[StoriesDAO].toInstance(new StoriesDAO))
  }
}