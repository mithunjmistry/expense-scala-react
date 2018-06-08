package services

import com.google.inject.ImplementedBy
import model.{Expense, ExpenseType, User}

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseServiceImpl])
trait ExpenseService {
  def addExpense(expense: Expense): Future[String]
  def deleteExpense(id: Int): Future[Int]
  def getExpense(id: Int): Future[Option[Expense]]
  def listAllExpenses(userID: Int): Future[Seq[(Expense, ExpenseType, User)]]
  def updateExpense(id: Int, expense: Expense) : Future[String]
}
