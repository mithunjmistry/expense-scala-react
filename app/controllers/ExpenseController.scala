package controllers

import java.sql.Timestamp

import javax.inject._
import model.Expense
import play.api.libs.json.Json
import play.api.mvc._
import services.ExpenseService
import model.Formatters._

import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.Logger
import views.html.defaultpages.badRequest
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date

import org.joda.time.DateTime

import scala.language.implicitConversions

@Singleton
class ExpenseController @Inject()(cc: ControllerComponents, expenseService: ExpenseService) extends AbstractController(cc) {

  def allExpenses(sortColumn: Option[String], sortDirection: Option[String], etype: Option[String] = None, month: Option[String] = None) =
    Action.async { implicit request => {
      expenseService.listAllExpenses(1, (sortColumn.getOrElse("created_at"), sortDirection.getOrElse("desc")), etype, month) map { expenses =>
          Ok(Json.toJson(expenses))
        }
    }
  }

  def addExpense = Action(parse.json) { request => {

//      implicit def stringToDouble(x: String) : Double = augmentString(x).toDouble
//      implicit def stringToInt(x: String) : Int = augmentString(x).toInt

      val expense_name = (request.body \ "expense_name").as[String]
      val description = (request.body \ "description").as[String]
      val amount = (request.body \ "amount").as[String].toDouble
      val user_id = (request.body \ "user_id").as[Int]
      val expense_type = (request.body \ "expenseType").as[String]
      val created_at_json = (request.body \ "date").as[String]
//      val date : Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(created_at_json)
//      val currentTimestamp = new Timestamp(date.getTime)
      val date : DateTime = DateTime.parse(created_at_json)
      try{
        expenseService.addExpense(expense_name, Some(description), amount, date, expense_type)
        Ok(Json.toJson("Expense added successfully"))
      }
      catch {
        case ex : Exception => InternalServerError("Expense cannot be added.")
      }
    }
  }

  def deleteExpense(id: Int) = Action {
    try{
      expenseService.deleteExpense(id)
      Ok("Expense deleted successfully")
    }
    catch {
      case ex : Exception => InternalServerError("Expense cannot be deleted")
    }
  }

  def getExpense(id: Int) = Action.async { implicit request => {
      expenseService.getExpense(id) map { e =>
        e match {
          case Some(expense) => Ok(Json.toJson(expense))
          case _ => BadRequest("Expense does not exist.")
        }
      }
    }
  }

  def updateExpense(id: Int) = Action(parse.tolerantFormUrlEncoded) { request => {
      try {
        val expense_name = request.body.get("expense_name").map(_.head).getOrElse("")
        val description : String = request.body.get("description").map(_.head).getOrElse("")
        val amount = request.body.get("amount").map(_.head).getOrElse("").toDouble
        val user_id = request.body.get("user_id").map(_.head).getOrElse("").toInt
        val expense_type_id = request.body.get("expense_type_id").map(_.head).getOrElse("").toInt
        val calendar = Calendar.getInstance
        val now = calendar.getTime
        val currentTimestamp = new Timestamp(now.getTime)
        expenseService.updateExpense(id, Expense(0, expense_name, Some(description), amount, Some(DateTime.now), null, user_id, expense_type_id))
        Ok("Expense updated successfully")
      }
      catch {
        case ex: Exception => {
          Logger.error(ex.getMessage)
          InternalServerError("Expense cannot be updated")
        }
      }
    }
  }
}
