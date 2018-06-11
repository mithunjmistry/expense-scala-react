package dao

import java.sql.Timestamp

import com.google.inject.ImplementedBy
import model.{Expense, ExpenseType, User}

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseDAOImpl])
trait ExpenseDAO {
  def add(expenseName: String, description: String, amount: Double, date: Timestamp, expense_type: String, user_id: Int): Future[String]
  def delete(id: Int): Future[Int]
  def get(id: Int): Future[Option[Expense]]
  def listAllExpenses(userID: Int): Future[Seq[(Expense, ExpenseType, User)]]
  def update(id: Int, expense: Expense) : Future[String]
  def getAllDates : Vector[String]
}
