

package parsers

import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VdmDateParser {
  val formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy HH:mm").withLocale(Locale.FRENCH);
  def parse(dateStr: String): LocalDateTime = LocalDateTime.parse(dateStr, formatter);
}