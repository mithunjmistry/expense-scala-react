package services

import com.google.inject.ImplementedBy
import model.Expense

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseServiceImpl])
trait ExpenseService {
  def addExpense(expense: Expense): Future[String]
  def deleteExpense(id: Int): Future[Int]
  def getExpense(id: Int): Future[Option[Expense]]
  def listAllExpenses: Future[Seq[Expense]]
  def updateExpense(id: Int, expense: Expense) : Future[String]
}
