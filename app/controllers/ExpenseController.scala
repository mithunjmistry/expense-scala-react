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

import scala.language.implicitConversions

@Singleton
class ExpenseController @Inject()(cc: ControllerComponents, expenseService: ExpenseService) extends AbstractController(cc) {

  def allExpenses = Action.async { implicit request => {
    expenseService.listAllExpenses(1) map { expenses =>
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
      val expense_type_id = (request.body \ "expense_type_id").as[Int]
      val calendar = Calendar.getInstance
      val now = calendar.getTime
      val currentTimestamp = new Timestamp(now.getTime)
      try{
        expenseService.addExpense(Expense(0, expense_name, Some(description), amount, Some(currentTimestamp), null, user_id, expense_type_id))
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
        expenseService.updateExpense(id, Expense(0, expense_name, Some(description), amount, Some(currentTimestamp), null, user_id, expense_type_id))
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
