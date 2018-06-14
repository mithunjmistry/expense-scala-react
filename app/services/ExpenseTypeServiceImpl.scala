package services

import dao.{ExpenseDAO, ExpenseTypeDAO}
import javax.inject.{Inject, Singleton}
import model.ExpenseType

import scala.concurrent.Future

@Singleton
class ExpenseTypeServiceImpl @Inject()(expenseTypeDAO: ExpenseTypeDAO) extends ExpenseTypeService {
  def addExpense(expenseType: ExpenseType): Future[String] = {
    expenseTypeDAO.add(expenseType)
  }

  def deleteExpense(id: Int): Future[Int] = {
    expenseTypeDAO.delete(id)
  }

  def getExpense(id: Int): Future[Option[ExpenseType]] = {
    expenseTypeDAO.get(id)
  }

  def listAllExpenseTypes: Future[Seq[String]] = {
    expenseTypeDAO.listAllExpenseTypes
  }

  def updateExpense(id: Int, expenseType: ExpenseType) : Future[String] = {
    expenseTypeDAO.update(id, expenseType)
  }
}
