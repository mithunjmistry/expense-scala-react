package model

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

case class Expense(id: Int, expense_name: String, description: Option[String] = None, amount: Double, created_at: Option[Timestamp], updated_at: Option[Timestamp], user_id: Int, expense_type_id: Int = 1)

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
  implicit val userFormat = Json.format[User]
  implicit val expenseTypeFormat = Json.format[ExpenseType]
}