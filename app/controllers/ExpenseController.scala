package controllers

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

@Singleton
class ExpenseController @Inject()(cc: ControllerComponents, expenseService: ExpenseService) extends AbstractController(cc) {

  def allExpenses = Action.async { implicit request => {
    expenseService.listAllExpenses map { implicit expenses =>
        Ok(Json.toJson(expenses))
      }
    }
  }

  def addExpense = Action(parse.tolerantFormUrlEncoded) { request => {
      val name = request.body.get("name").map(_.head).getOrElse("")
      val description = request.body.get("description").map(_.head).getOrElse("")
      val amount = request.body.get("amount").map(_.head).getOrElse("")
      try{
        expenseService.addExpense(Expense(0, name, description, amount.toFloat, null, null))
        Ok(Json.toJson("Expense added successfully"))
      }
      catch {
        case ex : Exception => InternalServerError("Expense cannot be added.")
      }
    }
  }

  def deleteExpense(id: Long) = Action {
    try{
      expenseService.deleteExpense(id)
      Ok("Expense deleted successfully")
    }
    catch {
      case ex : Exception => InternalServerError("Expense cannot be deleted")
    }
  }

  def updateExpense(id: Long) = Action(parse.tolerantFormUrlEncoded) { request => {
      try {
        val name = request.body.get("name").map(_.head).getOrElse("")
        val description = request.body.get("description").map(_.head).getOrElse("")
        val amount = request.body.get("amount").map(_.head).getOrElse("")
        expenseService.updateExpense(id, Expense(id, name, description, amount.toFloat, null, null))
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
