package dao

import java.sql.Timestamp

import com.google.inject.ImplementedBy
import model.{Expense, ExpenseType, User}
import org.joda.time.DateTime

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseDAOImpl])
trait ExpenseDAO {
  def add(expenseName: String, description: String, amount: Double, date: DateTime, expense_type: String, user_id: Int): Future[String]
  def delete(id: Int): Future[Int]
  def get(id: Int, userID: Int): Future[Option[(Expense, ExpenseType, User)]]
  def listAllExpenses(userID: Int, sorting: (String, String), etype: Option[String], month: Option[String]): Future[Seq[(Expense, ExpenseType, User)]]
  def update(id: Int, expenseName: String, description: String, amount: Double, date: DateTime, expense_type: String, user_id: Int) : Future[String]
  def getAllDates : Vector[String]
  def getExpenseStatistics(date: Option[String]) : Future[(Option[Double], Option[Double])]
}
