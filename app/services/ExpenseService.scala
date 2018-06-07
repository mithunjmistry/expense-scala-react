package services

import com.google.inject.ImplementedBy
import model.Expense

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseServiceImpl])
trait ExpenseService {
  def addExpense(expense: Expense): Future[String]
  def deleteExpense(id: Long): Future[Int]
  def getExpense(id: Long): Future[Option[Expense]]
  def listAllExpenses: Future[Seq[Expense]]
  def updateExpense(id: Long, expense: Expense) : Future[String]
}
