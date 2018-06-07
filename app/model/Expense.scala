package model

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

case class Expense(id: Long, name: String, description: String, amount: Float, created_at: Timestamp, updated_at: Timestamp)

object Formatters {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  implicit val expenseFormat = Json.format[Expense]
}