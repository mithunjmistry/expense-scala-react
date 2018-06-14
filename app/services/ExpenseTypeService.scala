package services

import com.google.inject.ImplementedBy
import model.ExpenseType

import scala.concurrent.Future

@ImplementedBy(classOf[ExpenseTypeServiceImpl])
trait ExpenseTypeService {
  def addExpense(expenseType: ExpenseType): Future[String]
  def deleteExpense(id: Int): Future[Int]
  def getExpense(id: Int): Future[Option[ExpenseType]]
  def listAllExpenseTypes: Future[Seq[String]]
  def updateExpense(id: Int, expenseType: ExpenseType) : Future[String]
}
