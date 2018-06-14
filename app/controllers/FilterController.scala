package controllers

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject._
import model.Expense
import model.Formatters._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import services.{ExpenseService, ExpenseTypeService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.language.implicitConversions
import scala.concurrent.duration._

@Singleton
class FilterController @Inject()(cc: ControllerComponents, expenseTypeService: ExpenseTypeService, expenseService: ExpenseService) extends AbstractController(cc) {

  def allExpenseTypes = Action.async { implicit request => {
    expenseTypeService.listAllExpenseTypes map { expenseTypes =>
        Ok(Json.toJson(expenseTypes))
      }
    }
  }

  def filterAttributes = Action { implicit request => {
      val dates = Json.toJson(expenseService.getAllDates)
      val expenseTypes = expenseTypeService.listAllExpenseTypes map {
          expenseType => Json.toJson(expenseType)
      }
      val et = Await.result(expenseTypes, 2 seconds)
      val json : JsValue = JsObject(Seq(
        "dates" -> dates,
        "expenseTypes" -> et
      ))
      Ok(json)

//    val json: JsValue = JsObject(Seq(
//      "name" -> JsString("Watership Down"),
//      "location" -> JsObject(Seq("lat" -> JsNumber(51.235685), "long" -> JsNumber(-1.309197))),
//      "residents" -> JsArray(IndexedSeq(
//        JsObject(Seq(
//          "name" -> JsString("Fiver"),
//          "age" -> JsNumber(4),
//          "role" -> JsNull
//        )),
//        JsObject(Seq(
//          "name" -> JsString("Bigwig"),
//          "age" -> JsNumber(6),
//          "role" -> JsString("Owsla")
//        ))
//      ))
//    ))
    }
  }

  def getStatistics(date: Option[String]) = Action.async { implicit request => {
      expenseService.getExpenseStatistics(date).map(s => s match
      {
        case (Some(m), Some(t)) => Ok(Json.toJson((m, t)))
        case _ => BadRequest("No expenses available")
      }
      )
    }
  }
}
