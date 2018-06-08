package services

import dao.ExpenseDAO
import model.Expense
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class ExpenseServiceImpl @Inject()(expenseDAO: ExpenseDAO) extends ExpenseService {
  def addExpense(expense: Expense): Future[String] = {
    expenseDAO.add(expense)
  }

  def deleteExpense(id: Int): Future[Int] = {
    expenseDAO.delete(id)
  }

  def getExpense(id: Int): Future[Option[Expense]] = {
    expenseDAO.get(id)
  }

  def listAllExpenses: Future[Seq[Expense]] = {
    expenseDAO.listAllExpenses
  }

  def updateExpense(id: Int, expense: Expense) : Future[String] = {
    expenseDAO.update(id, expense)
  }
}
