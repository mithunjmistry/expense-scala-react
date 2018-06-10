package services

import java.sql.Timestamp

import dao.ExpenseDAO
import model.{Expense, ExpenseType, User}
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class ExpenseServiceImpl @Inject()(expenseDAO: ExpenseDAO) extends ExpenseService {
  def addExpense(expenseName: String, description: Option[String] = None, amount: Double, date: Timestamp, expense_type: String, user_id: Int = 1): Future[String] = {
    expenseDAO.add(expenseName, description.getOrElse(""), amount, date, expense_type, user_id)
  }

  def deleteExpense(id: Int): Future[Int] = {
    expenseDAO.delete(id)
  }

  def getExpense(id: Int): Future[Option[Expense]] = {
    expenseDAO.get(id)
  }

  def listAllExpenses(userID: Int): Future[Seq[(Expense, ExpenseType, User)]] = {
    expenseDAO.listAllExpenses(userID)
  }

  def updateExpense(id: Int, expense: Expense) : Future[String] = {
    expenseDAO.update(id, expense)
  }
}
