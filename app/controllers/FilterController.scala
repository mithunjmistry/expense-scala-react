package controllers

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject._
import model.Expense
import model.Formatters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.{ExpenseService, ExpenseTypeService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.language.implicitConversions

@Singleton
class FilterController @Inject()(cc: ControllerComponents, expenseTypeService: ExpenseTypeService) extends AbstractController(cc) {

  def allExpenseTypes = Action.async { implicit request => {
    expenseTypeService.listAllExpenseTypes map { expenseTypes =>
        Ok(Json.toJson(expenseTypes))
      }
    }
  }
}
