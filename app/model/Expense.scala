package model

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.joda.time.DateTime
import play.api.libs.json._


case class Expense(id: Int, expense_name: String, description: Option[String] = None, amount: Double, created_at: Option[DateTime], updated_at: Option[DateTime], user_id: Int, expense_type_id: Int = 1)

object Formatters {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  import play.api.libs.json.JodaWrites
  implicit val dateTimeWriter: Writes[DateTime] = JodaWrites.jodaDateWrites("MM-dd-yyyy")
  import play.api.libs.json.JodaReads
  implicit val dateTimeJsReader = JodaReads.jodaDateReads("yyyyMMddHHmmss")

  implicit val expenseFormat = Json.format[Expense]
  implicit val userFormat = Json.format[User]
  implicit val expenseTypeFormat = Json.format[ExpenseType]
}