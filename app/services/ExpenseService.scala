package services

import java.sql.Timestamp

import com.google.inject.ImplementedBy
import model.{Expense, ExpenseType, User}
import org.joda.time.DateTime

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseServiceImpl])
trait ExpenseService {
  def addExpense(expenseName: String, description: Option[String] = None, amount: Double, date: DateTime, expense_type: String, user_id: Int = 1): Future[String]
  def deleteExpense(id: Int): Future[Int]
  def getExpense(id: Int): Future[Option[Expense]]
  def listAllExpenses(userID: Int, sorting: (String, String), etype: Option[String] = None, month: Option[String] = None): Future[Seq[(Expense, ExpenseType, User)]]
  def updateExpense(id: Int, expense: Expense) : Future[String]
  def getAllDates : Vector[String]
}
